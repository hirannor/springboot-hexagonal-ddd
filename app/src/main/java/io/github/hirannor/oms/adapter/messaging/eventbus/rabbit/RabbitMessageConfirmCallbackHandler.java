package io.github.hirannor.oms.adapter.messaging.eventbus.rabbit;

import io.github.hirannor.oms.application.port.outbox.Outbox;
import io.github.hirannor.oms.infrastructure.messaging.MessageId;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
class RabbitMessageConfirmCallbackHandler
        implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnsCallback {

    private static final Logger LOGGER = LogManager.getLogger(RabbitMessageConfirmCallbackHandler.class);

    private final Outbox outbox;

    RabbitMessageConfirmCallbackHandler(final Outbox outbox) {
        this.outbox = outbox;
    }

    @Override
    @Transactional
    public void confirm(final CorrelationData data, boolean ack, final String cause) {
        if (data == null) return;

        final String id = data.getId();

        if (ack) {
            outbox.markAsProcessed(MessageId.from(id));
            LOGGER.debug("ACK from RabbitMQ, marked message {} as processed", id);
        } else {
            LOGGER.error("NACK from RabbitMQ for message {}, cause={}", id, cause);
        }
    }

    @Override
    public void returnedMessage(final ReturnedMessage returned) {
        LOGGER.error("Message returned: {}", returned);
    }
}
