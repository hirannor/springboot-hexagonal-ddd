package io.github.hirannor.oms.adapter.messaging.eventbus.rabbit.message.mapping;

import io.github.hirannor.oms.adapter.messaging.eventbus.rabbit.message.DomainEventMapper;
import io.github.hirannor.oms.adapter.messaging.eventbus.rabbit.message.DomainEventModel;
import io.github.hirannor.oms.infrastructure.event.DomainEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class DomainEventToModelMapper implements Function<DomainEvent, DomainEventModel> {

    private final Map<Class<? extends DomainEvent>, DomainEventMapper<?, ?>> registry;

    @Autowired
    public DomainEventToModelMapper(final List<DomainEventMapper<?, ?>> mappers) {
        this.registry = mappers.stream()
            .collect(Collectors.toMap(DomainEventMapper::eventType, m -> m));
    }

    @Override
    @SuppressWarnings("unchecked")
    public DomainEventModel apply(final DomainEvent event) {
        if (event == null) return null;

        return ((DomainEventMapper<DomainEvent, DomainEventModel>)
                registry.get(event.getClass()))
                .apply(event);
    }
}
