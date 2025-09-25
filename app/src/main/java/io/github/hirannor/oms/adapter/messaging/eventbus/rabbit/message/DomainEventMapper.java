package io.github.hirannor.oms.adapter.messaging.eventbus.rabbit.message;

import io.github.hirannor.oms.infrastructure.event.DomainEvent;

import java.util.function.Function;

public interface DomainEventMapper<E extends DomainEvent, M extends DomainEventModel>
        extends Function<E, M> {

    Class<E> eventType();
}