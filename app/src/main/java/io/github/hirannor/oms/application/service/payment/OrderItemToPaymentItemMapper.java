package io.github.hirannor.oms.application.service.payment;

import io.github.hirannor.oms.application.port.payment.PaymentItem;
import io.github.hirannor.oms.domain.order.OrderItem;
import io.github.hirannor.oms.domain.product.Product;
import io.github.hirannor.oms.domain.product.ProductId;

import java.util.Map;
import java.util.function.BiFunction;

public class OrderItemToPaymentItemMapper implements BiFunction<OrderItem, Map<ProductId, Product>, PaymentItem> {

    public OrderItemToPaymentItemMapper() {
    }

    @Override
    public PaymentItem apply(final OrderItem item, final Map<ProductId, Product> products) {
        if (item == null) return null;

        final Product product = products.get(item.productId());

        return PaymentItem.create(
                item.productId(),
                item.quantity(),
                product.name(),
                item.price()
        );
    }
}
