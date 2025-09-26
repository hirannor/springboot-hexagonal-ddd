package io.github.hirannor.oms.adapter.persistence.jpa.outbox;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.hirannor.oms.adapter.persistence.jpa.outbox.message.MessageModel;
import io.github.hirannor.oms.application.port.outbox.Outbox;
import io.github.hirannor.oms.infrastructure.adapter.PersistenceAdapter;
import io.github.hirannor.oms.infrastructure.messaging.Message;
import io.github.hirannor.oms.infrastructure.messaging.MessageId;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.List;
import java.util.function.Function;

@PersistenceAdapter
class OutboxJpaRepository implements Outbox {
    private static final Logger LOGGER = LogManager.getLogger(
            OutboxJpaRepository.class
    );

    private final Function<MessageModel, Message> mapToMessage;
    private final Function<Message, MessageModel> mapToModel;
    private final OutboxSpringDataJpaRepository outboxes;
    private final ObjectMapper mapper;

    @Autowired
    OutboxJpaRepository(final OutboxSpringDataJpaRepository outboxes,
                        final ObjectMapper mapper,
                        final Function<MessageModel, Message> mapToMessage,
                        final Function<Message, MessageModel> mapToModel
                        ) {
        this.outboxes = outboxes;
        this.mapper = mapper;
        this.mapToMessage = mapToMessage;
        this.mapToModel = mapToModel;
    }

    @Override
    public void save(final Message msg) {
        if (msg == null) throw new IllegalArgumentException("Message cannot be null!");

        try {
            final MessageModel message = mapToModel.apply(msg);

            if (message == null) return;

            final String payload = mapper.writeValueAsString(message);

            final OutboxModel model = new OutboxModel();
            model.setMessageId(message.id().asText());
            model.setMessageType(message.getClass().getName());
            model.setProcessed(false);
            model.setCreatedAt(Instant.now());
            model.setPayload(payload);

            outboxes.save(model);
        } catch (final JsonProcessingException ex) {
            LOGGER.error("Failed to serialize msg {}", msg.getClass().getSimpleName(), ex);
            throw new IllegalStateException("Cannot save msg to outbox", ex);
        }
    }

    @Override
    public List<Message> findAllUnprocessed(int batchSize) {
        if (batchSize <= 0) throw new IllegalArgumentException("Batch size must be greater than 0");

        final Pageable pageable = PageRequest.of(0, batchSize);

        return outboxes.findByProcessedFalseOrderByCreatedAtAsc(pageable)
                .stream()
                .map(this::toMessageModel)
                .map(mapToMessage)
                .toList();
    }

    @Override
    public void markAsProcessed(final MessageId id) {
        if (id == null) throw new IllegalArgumentException("Id cannot be null!");

        outboxes.findByMessageId(id.asText())
                .ifPresent(model -> {
                    model.setProcessed(true);
                    outboxes.save(model);
                });
    }

    @Override
    public void deleteProcessedOlderThan(final Instant time) {
        if (time == null) throw new IllegalArgumentException("time cannot be null");

        outboxes.deleteByProcessedIsTrueAndCreatedAtBefore(time);
    }

    private MessageModel toMessageModel(final OutboxModel model) {
        try {
            final Class<?> clazz = Class.forName(model.getMessageType());
            return (MessageModel) mapper.readValue(model.getPayload(), clazz);
        } catch (final JsonProcessingException | ClassNotFoundException ex) {
            LOGGER.error("Failed to serialize message {}", model.getClass().getSimpleName(), ex);
            throw new RuntimeException(ex);
        }
    }
}
