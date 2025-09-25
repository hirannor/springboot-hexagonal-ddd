package io.github.hirannor.oms.adapter.messaging.eventbus.rabbit.message.mapping;

import io.github.hirannor.oms.adapter.messaging.eventbus.rabbit.message.DomainEventModel;
import io.github.hirannor.oms.adapter.messaging.eventbus.rabbit.message.DomainEventModelMapper;
import io.github.hirannor.oms.infrastructure.event.DomainEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class DomainEventModelToEventMapper implements Function<DomainEventModel, DomainEvent> {

    private final Map<Class<? extends DomainEventModel>, DomainEventModelMapper<?, ?>> registry;

    @Autowired
    public DomainEventModelToEventMapper(final List<DomainEventModelMapper<?, ?>> mappers) {
        this.registry = mappers.stream()
                .collect(Collectors.toMap(DomainEventModelMapper::eventType, m -> m));
    }

    @Override
    @SuppressWarnings("unchecked")
    public DomainEvent apply(final DomainEventModel model) {
        if (model == null) return null;

        return ((DomainEventModelMapper<DomainEventModel, DomainEvent>)
                registry.get(model.getClass()))
                .apply(model);
    }
}
