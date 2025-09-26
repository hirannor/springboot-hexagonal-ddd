package io.github.hirannor.oms.adapter.messaging.eventbus.rabbit.message;

import io.github.hirannor.oms.infrastructure.messaging.Message;

import java.util.function.Function;

public interface MessageModelMapper<E extends MessageModel, M extends Message>
        extends Function<E, M> {

    Class<E> messageType();
}