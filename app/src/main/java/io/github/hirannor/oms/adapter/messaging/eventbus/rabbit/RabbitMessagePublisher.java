package io.github.hirannor.oms.adapter.messaging.eventbus.rabbit;

import io.github.hirannor.oms.adapter.messaging.eventbus.rabbit.message.MessageModel;
import io.github.hirannor.oms.infrastructure.adapter.DrivenAdapter;
import io.github.hirannor.oms.infrastructure.messaging.Message;
import io.github.hirannor.oms.infrastructure.messaging.MessagePublisher;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
@DrivenAdapter
class RabbitMessagePublisher implements MessagePublisher {

    private static final Logger LOGGER = LogManager.getLogger(RabbitMessagePublisher.class);
    private static final String ROUTING_KEY = "oms.events";

    private final RabbitTemplate rabbitTemplate;
    private final RabbitConfigurationProperties properties;
    private final Function<Message, MessageModel> mapToModel;


    RabbitMessagePublisher(final RabbitTemplate rabbitTemplate,
                           final RabbitConfigurationProperties properties,
                           final Function<Message, MessageModel> mapToModel) {
        this.rabbitTemplate = rabbitTemplate;
        this.properties = properties;
        this.mapToModel = mapToModel;
    }

    @Override
    public void publish(final Message message) {
        LOGGER.debug("Received message for publishing: {}", message);

        final MessageModel model = mapToModel.apply(message);

        rabbitTemplate.convertAndSend(
                properties.getExchange(),
                ROUTING_KEY,
                model
        );

        LOGGER.debug("Successfully published message [{}] to exchange [{}] with routing key [{}]",
                model.getClass().getSimpleName(),
                properties.getExchange(),
                model.getClass().getSimpleName());
    }
}
