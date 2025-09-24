package io.github.hirannor.oms.adapter.persistence.jpa.outbox.order;

import io.github.hirannor.oms.adapter.persistence.jpa.outbox.DomainEventModel;
import io.github.hirannor.oms.adapter.persistence.jpa.outbox.ProductQuantityModel;

import java.util.List;

public record OrderPaidModel(
        String eventId,
        String orderId,
        String customerId,
        List<ProductQuantityModel> products
) implements DomainEventModel {
}
