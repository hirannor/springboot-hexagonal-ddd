package io.github.hirannor.oms.adapter.messaging.eventbus.rabbit.message.order;

import io.github.hirannor.oms.adapter.messaging.eventbus.rabbit.message.DomainEventModel;
import io.github.hirannor.oms.adapter.messaging.eventbus.rabbit.message.ProductQuantityModel;

import java.util.List;

public record OrderPaymentFailedModel(
        String eventId,
        String orderId,
        String customerId,
        List<ProductQuantityModel> products
) implements DomainEventModel {
}
