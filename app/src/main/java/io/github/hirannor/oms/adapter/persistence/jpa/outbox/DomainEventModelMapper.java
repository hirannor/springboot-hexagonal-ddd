package io.github.hirannor.oms.adapter.persistence.jpa.outbox;

import io.github.hirannor.oms.infrastructure.event.DomainEvent;

import java.util.function.Function;

public interface DomainEventModelMapper<E extends DomainEventModel, M extends DomainEvent>
        extends Function<E, M> {

    Class<E> eventType();
}