package io.github.hirannor.oms.adapter.messaging.eventbus.message.order;

import io.github.hirannor.oms.adapter.messaging.eventbus.message.DomainEventModel;
import io.github.hirannor.oms.adapter.messaging.eventbus.message.ProductQuantityModel;

import java.util.List;

public record OrderPaidModel(
        String eventId,
        String orderId,
        String customerId,
        List<ProductQuantityModel> products
) implements DomainEventModel {
}
