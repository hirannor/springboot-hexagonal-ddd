package io.github.hirannor.oms.adapter.persistence.jpa.outbox.message.order;

import io.github.hirannor.oms.adapter.persistence.jpa.outbox.message.MessageModel;
import io.github.hirannor.oms.infrastructure.messaging.MessageId;

public record OrderShippedModel(
        MessageId id,
        String orderId,
        String customerId
) implements MessageModel {
}
