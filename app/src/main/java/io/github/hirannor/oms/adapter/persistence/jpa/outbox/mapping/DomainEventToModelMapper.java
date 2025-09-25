package io.github.hirannor.oms.adapter.persistence.jpa.outbox.mapping;

import io.github.hirannor.oms.adapter.persistence.jpa.outbox.DomainEventMapper;
import io.github.hirannor.oms.adapter.persistence.jpa.outbox.DomainEventModel;
import io.github.hirannor.oms.infrastructure.event.DomainEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component(value = "DomainEventToModelMapper")
public class DomainEventToModelMapper implements Function<DomainEvent, DomainEventModel> {

    private final Map<Class<? extends DomainEvent>, DomainEventMapper<?, ?>> registry;

    @Autowired
    public DomainEventToModelMapper(final List<DomainEventMapper<?, ?>> mappers) {
        this.registry = mappers.stream()
            .collect(Collectors.toMap(DomainEventMapper::eventType, m -> m));
    }

    @Override
    public DomainEventModel apply(final DomainEvent event) {
        if (event == null) return null;

        final DomainEventMapper<DomainEvent, DomainEventModel> mapper = (DomainEventMapper<DomainEvent, DomainEventModel>) registry.get(event.getClass());

        if (mapper == null) return null;

        return mapper.apply(event);
    }
}
