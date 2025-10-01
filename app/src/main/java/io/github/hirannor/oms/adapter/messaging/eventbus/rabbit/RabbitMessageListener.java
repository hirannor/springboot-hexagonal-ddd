package io.github.hirannor.oms.adapter.messaging.eventbus.rabbit;

import com.rabbitmq.client.Channel;
import io.github.hirannor.oms.adapter.messaging.eventbus.rabbit.message.MessageModel;
import io.github.hirannor.oms.application.port.outbox.Outbox;
import io.github.hirannor.oms.infrastructure.adapter.DriverAdapter;
import io.github.hirannor.oms.infrastructure.messaging.Message;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.io.IOException;
import java.util.function.Function;

@Component
@DriverAdapter
class RabbitMessageListener {
    private static final Logger LOGGER = LogManager.getLogger(RabbitMessageListener.class);

    private final ApplicationEventPublisher internalBus;
    private final Function<MessageModel, Message> mapToMessage;
    private final TransactionTemplate txTemplate;
    private final Outbox outbox;

    RabbitMessageListener(final ApplicationEventPublisher internalBus,
                          final Function<MessageModel, Message> mapToMessage,
                          final PlatformTransactionManager txManager,
                          final Outbox outbox) {
        this.internalBus = internalBus;
        this.mapToMessage = mapToMessage;
        this.txTemplate = new TransactionTemplate(txManager);
        this.outbox = outbox;
    }

    @RabbitListener(queues = "${messaging.rabbit.queue}", ackMode = "MANUAL")
    void onMessage(final Message message,
                   final Channel channel,
                   final org.springframework.amqp.core.Message amqpMessage) throws IOException {
        if (!(message instanceof MessageModel model)) {
            LOGGER.warn("Received unexpected message type: {}", message.getClass().getName());
            return;
        }

        long tag = amqpMessage.getMessageProperties().getDeliveryTag();

        try {
            txTemplate.executeWithoutResult(tx -> {
                LOGGER.debug("Received {} with id {} from RabbitMQ",
                        model.getClass().getSimpleName(),
                        model.id().asText());

                final Message domainMessage = mapToMessage.apply(model);

                outbox.markAsProcessed(domainMessage.id());

                internalBus.publishEvent(domainMessage);

                LOGGER.debug("Published {} with id {} to internal bus + saved to Outbox",
                        domainMessage.getClass().getSimpleName(),
                        domainMessage.id().asText());
            });

            channel.basicAck(tag, false);
            LOGGER.debug("ACK sent to RabbitMQ for message {}", model.id().asText());

        } catch (final Exception ex) {
            LOGGER.error("Failed processing RabbitMQ message {}, rolling back + requeue. cause={}",
                    model.id().asText(), ex.getMessage(), ex);
            channel.basicNack(tag, false, true);
        }
    }
}
