package io.github.hirannor.oms.adapter.messaging.eventbus.rabbit;

import io.github.hirannor.oms.application.port.outbox.Outbox;
import io.github.hirannor.oms.infrastructure.messaging.Message;
import io.github.hirannor.oms.infrastructure.messaging.MessagePublisher;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Consumer;

@Component
class OutboxMessagePublisher {

    private static final Logger LOGGER = LogManager.getLogger(OutboxMessagePublisher.class);

    private final Outbox outboxes;
    private final int batchSize;
    private final Consumer<Message> pipeline;

    @Autowired
    OutboxMessagePublisher(final Outbox outboxes,
                           final MessagePublisher messages,
                           final RabbitConfigurationProperties properties) {
        this.outboxes = outboxes;
        this.batchSize = properties.getOutbox().getBatchSize();
        this.pipeline = ((Consumer<Message>) messages::publish)
                .andThen(logMessages());
    }

    @Transactional
    @Scheduled(fixedDelayString = "${messaging.rabbit.outbox.poll-interval}")
    void publishOutboxMessages() {
        final List<Message> pendingMessages = outboxes.findAllPendingBy(batchSize);
        pendingMessages.forEach(pipeline);
    }

    private Consumer<Message> logMessages() {
        return msg -> LOGGER.debug(
                "Published {} with id {}",
                msg.getClass().getSimpleName(),
                msg.id().asText()
        );
    }
}
