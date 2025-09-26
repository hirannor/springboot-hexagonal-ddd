package io.github.hirannor.oms.adapter.messaging.eventbus.rabbit.message.order;

import io.github.hirannor.oms.adapter.messaging.eventbus.rabbit.message.MessageModel;
import io.github.hirannor.oms.infrastructure.messaging.MessageId;

public record OrderProcessingModel(
        MessageId id,
        String orderId,
        String customerId
) implements MessageModel {
}
