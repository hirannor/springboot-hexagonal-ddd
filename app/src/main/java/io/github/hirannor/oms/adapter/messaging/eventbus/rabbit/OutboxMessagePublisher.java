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

@Component
class OutboxMessagePublisher {

    private static final Logger LOGGER = LogManager.getLogger(OutboxMessagePublisher.class);

    private final Outbox outboxes;
    private final MessagePublisher messages;
    private final RabbitConfigurationProperties properties;

    @Autowired
    OutboxMessagePublisher(final Outbox outboxes,
                           final MessagePublisher messages,
                           final RabbitConfigurationProperties properties) {
        this.outboxes = outboxes;
        this.messages = messages;
        this.properties = properties;
    }

    @Transactional
    @Scheduled(fixedDelayString = "${messaging.rabbit.outbox.poll-interval}")
    void publishOutboxMessages() {
        final List<Message> unprocessedMessages = outboxes.findAllUnprocessed(properties.getOutbox().getBatchSize());

        for (final Message message : unprocessedMessages) {
            messages.publish(message);
            outboxes.markAsProcessed(message.id());

            LOGGER.debug("Published {} with id {}", message.getClass().getSimpleName(), message.id().asText());
        }
    }
}
