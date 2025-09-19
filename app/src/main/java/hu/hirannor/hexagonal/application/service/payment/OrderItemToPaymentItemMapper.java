package hu.hirannor.hexagonal.application.service.payment;

import hu.hirannor.hexagonal.application.port.payment.PaymentItem;
import hu.hirannor.hexagonal.domain.order.OrderItem;
import hu.hirannor.hexagonal.domain.product.Product;
import hu.hirannor.hexagonal.domain.product.ProductId;
import java.util.Map;
import java.util.function.BiFunction;

public class OrderItemToPaymentItemMapper implements BiFunction<OrderItem, Map<ProductId, Product>, PaymentItem> {

    public OrderItemToPaymentItemMapper() {}

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
