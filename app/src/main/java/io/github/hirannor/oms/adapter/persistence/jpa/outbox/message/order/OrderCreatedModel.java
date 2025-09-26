package io.github.hirannor.oms.adapter.persistence.jpa.outbox.message.order;


import io.github.hirannor.oms.adapter.persistence.jpa.outbox.message.MessageModel;

public record OrderCreatedModel(
        String id,
        String orderId,
        String customerId
) implements MessageModel {
}
