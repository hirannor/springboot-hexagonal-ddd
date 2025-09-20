package io.github.hirannor.oms.application.port.payment;

import io.github.hirannor.oms.domain.core.valueobject.Money;
import io.github.hirannor.oms.domain.product.ProductId;

public record PaymentItem(ProductId productId,
                          String productName,
                          int quantity,
                          Money price) {

    public static PaymentItem create(final ProductId productId,
                                     final int quantity,
                                     final String productName,
                                     final Money price) {
        return new PaymentItem(productId, productName, quantity, price);
    }
}
