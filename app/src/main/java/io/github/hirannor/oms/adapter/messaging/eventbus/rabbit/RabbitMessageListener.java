package io.github.hirannor.oms.adapter.messaging.eventbus.rabbit;

import io.github.hirannor.oms.adapter.messaging.eventbus.rabbit.message.MessageModel;
import io.github.hirannor.oms.infrastructure.adapter.DriverAdapter;
import io.github.hirannor.oms.infrastructure.messaging.Message;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
@DriverAdapter
class RabbitMessageListener {
    private static final Logger LOGGER = LogManager.getLogger(RabbitMessageListener.class);

    private final ApplicationEventPublisher internalBus;
    private final Function<MessageModel, Message> mapToMessage;

    RabbitMessageListener(final ApplicationEventPublisher internalBus,
                          final Function<MessageModel, Message> mapToMessage) {
        this.internalBus = internalBus;
        this.mapToMessage = mapToMessage;
    }

    @RabbitListener(queues = "${messaging.rabbit.queue}")
    void onMessage(final Message message) {
        if (!(message instanceof MessageModel model)) {
            LOGGER.warn("Received unexpected message type: {}", message.getClass().getName());
            return;
        }

        LOGGER.debug("Received model {} with id {} from RabbitMQ",
                model.getClass().getSimpleName(),
                model.id().asText());

        final Message messageToPublish = mapToMessage.apply(model);

        internalBus.publishEvent(messageToPublish);

        LOGGER.debug("Published {} with id {} to internal bus",
                messageToPublish.getClass().getSimpleName(),
                messageToPublish.id().asText());
    }
}
