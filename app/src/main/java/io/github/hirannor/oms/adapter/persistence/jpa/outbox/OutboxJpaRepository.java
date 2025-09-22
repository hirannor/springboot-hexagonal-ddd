package io.github.hirannor.oms.adapter.persistence.jpa.outbox;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.hirannor.oms.adapter.persistence.jpa.outbox.mapping.DomainEventModelToEventMapper;
import io.github.hirannor.oms.adapter.persistence.jpa.outbox.mapping.DomainEventToModelMapper;
import io.github.hirannor.oms.application.port.outbox.Outbox;
import io.github.hirannor.oms.infrastructure.adapter.PersistenceAdapter;
import io.github.hirannor.oms.infrastructure.event.DomainEvent;
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

    private final Function<DomainEventModel, DomainEvent> mapDomainEventModelToEvent;
    private final Function<DomainEvent, DomainEventModel> mapDomainEventToModelModel;
    private final OutboxSpringDataJpaRepository outboxes;
    private final ObjectMapper mapper;

    @Autowired
    OutboxJpaRepository(final OutboxSpringDataJpaRepository outboxes,
                               final ObjectMapper mapper) {
        this.outboxes = outboxes;
        this.mapper = mapper;
        this.mapDomainEventModelToEvent = new DomainEventModelToEventMapper();
        this.mapDomainEventToModelModel = new DomainEventToModelMapper();
    }

    @Override
    public void save(final DomainEvent evt) {
        if (evt == null) throw new IllegalArgumentException("DomainEvent cannot be null!");

        try {
            final DomainEventModel eventModel = mapDomainEventToModelModel.apply(evt);

            if (eventModel == null) return;

            final String payload = mapper.writeValueAsString(eventModel);

            final OutboxModel model = new OutboxModel();
            model.setEventId(eventModel.eventId());
            model.setEventType(eventModel.getClass().getName());
            model.setProcessed(false);
            model.setCreatedAt(Instant.now());
            model.setPayload(payload);

            outboxes.save(model);
        } catch (final JsonProcessingException ex) {
            LOGGER.error("Failed to serialize event {}", evt.getClass().getSimpleName(), ex);
            throw new IllegalStateException("Cannot save event to outbox", ex);
        }
    }

    @Override
    public List<DomainEvent> findUnprocessed(int batchSize) {
        final Pageable pageable = PageRequest.of(0, batchSize);

        return outboxes.findByProcessedFalseOrderByCreatedAtAsc(pageable)
                .stream()
                .map(this::toDomainEvent)
                .map(mapDomainEventModelToEvent)
                .toList();
    }

    @Override
    public void markAsProcessed(final MessageId id) {
        if (id == null) throw new IllegalArgumentException("Id cannot be null!");

        outboxes.findByEventId(id.asText())
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

    private DomainEventModel toDomainEvent(final OutboxModel model) {
        try {
            final Class<?> clazz = Class.forName(model.getEventType());
            return (DomainEventModel) mapper.readValue(model.getPayload(), clazz);
        } catch (final JsonProcessingException | ClassNotFoundException ex) {
            LOGGER.error("Failed to serialize event {}", model.getClass().getSimpleName(), ex);
            throw new RuntimeException(ex);
        }
    }
}
