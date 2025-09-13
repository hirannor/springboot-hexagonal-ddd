package hu.hirannor.hexagonal.application.service.payment;


import hu.hirannor.hexagonal.domain.Money;
import hu.hirannor.hexagonal.domain.order.OrderId;
import hu.hirannor.hexagonal.infrastructure.command.CommandId;

import java.util.Objects;

public record ProcessPayment(
        CommandId id,
        OrderId orderId,
        Money amount,
        String paymentMethodToken
) {

    public ProcessPayment {
        Objects.requireNonNull(id, "commandId must not be null");
        Objects.requireNonNull(orderId, "orderId must not be null");
        Objects.requireNonNull(amount, "amount must not be null");
        Objects.requireNonNull(paymentMethodToken, "paymentMethodToken must not be null");
    }

    public static ProcessPayment create(
            final OrderId orderId,
            final Money amount,
            final String paymentMethodToken
    ) {
        return new ProcessPayment(CommandId.generate(), orderId, amount, paymentMethodToken);
    }
}
