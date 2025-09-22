package io.github.hirannor.oms.adapter.messaging.eventbus.rabbit;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.hirannor.oms.adapter.messaging.eventbus.message.DomainEventModel;
import io.github.hirannor.oms.adapter.messaging.eventbus.message.mapping.DomainEventModelToEventMapper;
import io.github.hirannor.oms.infrastructure.event.DomainEvent;
import io.github.hirannor.oms.infrastructure.messaging.EventEnvelope;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
class RabbitMessageListener {
    private static final Logger LOGGER = LogManager.getLogger(RabbitMessageListener.class);

    private final ObjectMapper mapper;
    private final ApplicationEventPublisher internalBus;
    private final Function<DomainEventModel, DomainEvent> mapDomainEventModelToEvent;

    public RabbitMessageListener(final ObjectMapper mapper,
                                 final ApplicationEventPublisher internalBus) {
        this.mapper = mapper;
        this.internalBus = internalBus;
        this.mapDomainEventModelToEvent = new DomainEventModelToEventMapper();
    }

    @RabbitListener(queues = "${messaging.rabbit.queue}")
    void receive(final String json) {
        try {
            final EventEnvelope envelope = mapper.readValue(json, EventEnvelope.class);
            final Class<?> clazz = Class.forName(envelope.type());
            final DomainEventModel model = (DomainEventModel) mapper.readValue(envelope.payload(), clazz);

            final DomainEvent evt = mapDomainEventModelToEvent.apply(model);

            if (evt == null) {
                LOGGER.warn("Skipping unmapped model {}", clazz.getSimpleName());
                return;
            }

            LOGGER.debug("Received model {} with id {} from RabbitMQ",
                    clazz.getSimpleName(),
                    evt.id().asText());

            internalBus.publishEvent(evt);

            LOGGER.debug("Published model {} with id {} to internal bus",
                    clazz.getSimpleName(),
                    evt.id().asText());
        } catch (Exception e) {
            LOGGER.error("Failed to handle RabbitMQ message: {}", json, e);
            throw new IllegalStateException("Failed to consume RabbitMQ message", e);
        }
    }
}
