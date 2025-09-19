package hu.hirannor.hexagonal.application.port.payment;

import hu.hirannor.hexagonal.domain.core.valueobject.Money;
import hu.hirannor.hexagonal.domain.product.ProductId;

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
