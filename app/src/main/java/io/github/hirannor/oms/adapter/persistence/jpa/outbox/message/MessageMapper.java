package io.github.hirannor.oms.adapter.persistence.jpa.outbox.message;

import io.github.hirannor.oms.infrastructure.messaging.Message;

import java.util.function.Function;

public interface MessageMapper<E extends Message, M extends MessageModel>
        extends Function<E, M> {

    Class<E> messageType();
}