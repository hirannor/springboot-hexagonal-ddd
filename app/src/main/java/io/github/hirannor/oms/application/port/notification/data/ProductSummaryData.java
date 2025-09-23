package io.github.hirannor.oms.application.port.notification.data;

import io.github.hirannor.oms.domain.core.valueobject.Money;
import io.github.hirannor.oms.domain.order.OrderItem;
import io.github.hirannor.oms.domain.product.Product;

public record ProductSummaryData(
        String productId,
        String name,
        String description,
        int quantity,
        Money price
) {
    public static ProductSummaryData from(final OrderItem item, final Product product) {
        return new ProductSummaryData(
                item.productId().asText(),
                product.name(),
                product.description(),
                item.quantity(),
                item.price()
        );
    }
}
