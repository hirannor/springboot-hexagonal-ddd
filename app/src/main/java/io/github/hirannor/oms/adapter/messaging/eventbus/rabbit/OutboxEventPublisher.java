package io.github.hirannor.oms.adapter.messaging.eventbus.rabbit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.hirannor.oms.adapter.messaging.eventbus.rabbit.message.DomainEventModel;
import io.github.hirannor.oms.application.port.outbox.Outbox;
import io.github.hirannor.oms.infrastructure.event.DomainEvent;
import io.github.hirannor.oms.infrastructure.messaging.EventEnvelope;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Function;

@Component
class OutboxEventPublisher {

    private static final Logger LOGGER = LogManager.getLogger(OutboxEventPublisher.class);

    private final Outbox outboxes;
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper mapper;

    private final Function<DomainEvent, DomainEventModel> mapDomainEventToModel;
    private final RabbitConfigurationProperties properties;

    @Autowired
    OutboxEventPublisher(final Outbox outboxes,
                         final RabbitTemplate rabbitTemplate,
                         final ObjectMapper mapper,
                         final RabbitConfigurationProperties properties,
                         final Function<DomainEvent, DomainEventModel> mapDomainEventToModel) {
        this.outboxes = outboxes;
        this.rabbitTemplate = rabbitTemplate;
        this.mapper = mapper;
        this.properties = properties;
        this.mapDomainEventToModel = mapDomainEventToModel;
    }

    @Transactional
    @Scheduled(fixedDelayString = "${messaging.rabbit.outbox.poll-interval}")
    void publishOutboxEvents() {
        final int batchSize = properties.getOutbox().getBatchSize();
        final List<DomainEvent> events = outboxes.findAllUnprocessed(batchSize);

        for (final DomainEvent evt : events) {
            try {
                final DomainEventModel eventModel = mapDomainEventToModel.apply(evt);

                if (eventModel == null) {
                    LOGGER.warn("Skipping unmapped event");
                    continue;
                }

                final EventEnvelope envelope = new EventEnvelope(eventModel.getClass().getName(), mapper.writeValueAsString(eventModel));

                rabbitTemplate.convertAndSend(properties.getExchange(), eventModel.getClass().getSimpleName(), mapper.writeValueAsString(envelope));
                outboxes.markAsProcessed(evt.id());

                LOGGER.debug("Published event {} with id {}", evt.getClass().getSimpleName(), evt.id().asText());

            } catch (final JsonProcessingException ex) {
                LOGGER.error("Failed to publish event {} with id {}", evt.getClass().getSimpleName(), evt.id().asText(), ex);
            }
        }
    }

}
