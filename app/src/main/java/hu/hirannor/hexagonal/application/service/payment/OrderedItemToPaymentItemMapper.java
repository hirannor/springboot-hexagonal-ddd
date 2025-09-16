package hu.hirannor.hexagonal.application.service.payment;

import hu.hirannor.hexagonal.application.port.payment.PaymentItem;
import hu.hirannor.hexagonal.domain.order.OrderItem;

import java.util.function.Function;

public class OrderedItemToPaymentItemMapper implements Function<OrderItem, PaymentItem> {

    public OrderedItemToPaymentItemMapper() {}

    @Override
    public PaymentItem apply(final OrderItem product) {
        if (product == null) return null;

        return PaymentItem.create(
                product.productId(),
                product.quantity(),
                product.price()
        );
    }
}
