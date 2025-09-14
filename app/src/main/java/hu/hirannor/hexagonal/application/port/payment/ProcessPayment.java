package hu.hirannor.hexagonal.application.port.payment;


import hu.hirannor.hexagonal.domain.Money;
import hu.hirannor.hexagonal.domain.order.OrderId;
import hu.hirannor.hexagonal.infrastructure.command.CommandId;

import java.util.List;
import java.util.Objects;

public record ProcessPayment(
        CommandId id,
        OrderId orderId,
        List<PaymentItem> items,
        Money totalAmount,
        PaymentMethod method
) {

    public ProcessPayment {
        Objects.requireNonNull(id, "commandId must not be null");
        Objects.requireNonNull(orderId, "orderId must not be null");
        Objects.requireNonNull(totalAmount, "totalAmount must not be null");
        Objects.requireNonNull(method, "PaymentMethod must not be null");
    }

    public static ProcessPayment create(
            final OrderId orderId,
            final List<PaymentItem> items,
            final Money totalAmount,
            final PaymentMethod method
    ) {
        return new ProcessPayment(CommandId.generate(), orderId, items, totalAmount, method);
    }
}
