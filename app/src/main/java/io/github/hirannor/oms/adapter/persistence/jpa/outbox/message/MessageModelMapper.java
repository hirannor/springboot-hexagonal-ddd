package io.github.hirannor.oms.adapter.persistence.jpa.outbox.message;

import io.github.hirannor.oms.infrastructure.event.DomainEvent;

import java.util.function.Function;

public interface MessageModelMapper<E extends MessageModel, M extends DomainEvent>
        extends Function<E, M> {

    Class<E> messageType();
}