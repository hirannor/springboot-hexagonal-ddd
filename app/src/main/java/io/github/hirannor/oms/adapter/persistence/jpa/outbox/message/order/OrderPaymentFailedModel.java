package io.github.hirannor.oms.adapter.persistence.jpa.outbox.message.order;


import io.github.hirannor.oms.adapter.persistence.jpa.outbox.message.MessageModel;
import io.github.hirannor.oms.adapter.persistence.jpa.outbox.message.ProductQuantityModel;
import io.github.hirannor.oms.infrastructure.messaging.MessageId;

import java.util.List;

public record OrderPaymentFailedModel(
        MessageId id,
        String orderId,
        String customerId,
        List<ProductQuantityModel> products
) implements MessageModel {
}
