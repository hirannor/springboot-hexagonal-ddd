package io.github.hirannor.oms.adapter.messaging.eventbus.rabbit;

import io.github.hirannor.oms.adapter.messaging.eventbus.rabbit.message.MessageModel;
import io.github.hirannor.oms.application.service.outbox.OutboxProcessing;
import io.github.hirannor.oms.infrastructure.adapter.DriverAdapter;
import io.github.hirannor.oms.infrastructure.messaging.Message;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.function.Function;

@Component
@DriverAdapter
class RabbitMessageListener {
    private static final Logger LOGGER = LogManager.getLogger(RabbitMessageListener.class);

    private final ApplicationEventPublisher internalBus;
    private final Function<MessageModel, Message> mapToMessage;
    private final TransactionTemplate txTemplate;
    private final OutboxProcessing outbox;

    RabbitMessageListener(final ApplicationEventPublisher internalBus,
                          final Function<MessageModel, Message> mapToMessage,
                          final PlatformTransactionManager txManager,
                          final OutboxProcessing outbox) {
        this.internalBus = internalBus;
        this.mapToMessage = mapToMessage;
        this.txTemplate = new TransactionTemplate(txManager);
        this.outbox = outbox;
    }

    @RabbitListener(queues = "${messaging.rabbit.queue}")
    void onMessage(final Message message) {
        if (!(message instanceof MessageModel model)) {
            LOGGER.warn("Received unexpected message type: {}", message.getClass().getName());
            return;
        }

        try {
            txTemplate.executeWithoutResult(tx -> {
                LOGGER.debug("Received {} with id {} from RabbitMQ",
                        model.getClass().getSimpleName(),
                        model.id().asText());

                final Message domainMessage = mapToMessage.apply(model);

                internalBus.publishEvent(domainMessage);

                LOGGER.debug("Published {} with id {} to internal bus",
                        domainMessage.getClass().getSimpleName(),
                        domainMessage.id().asText());
            });

            outbox.markAsPublished(model.id());

        } catch (Exception e) {
            LOGGER.error("Failed to process message {} with id {}, will NOT mark as processed",
                    message.getClass().getSimpleName(), model.id().asText(), e);
            outbox.markAsFailed(model.id(), e);
            throw e;
        }
    }
}
