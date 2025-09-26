package io.github.hirannor.oms.adapter.messaging.eventbus.rabbit.message.order;

import io.github.hirannor.oms.adapter.messaging.eventbus.rabbit.message.MessageModel;
import io.github.hirannor.oms.adapter.messaging.eventbus.rabbit.message.ProductQuantityModel;
import io.github.hirannor.oms.infrastructure.messaging.MessageId;

import java.util.List;

public record OrderPaymentExpiredModel(
        MessageId id,
        String orderId,
        String customerId,
        List<ProductQuantityModel> products
) implements MessageModel {
}
