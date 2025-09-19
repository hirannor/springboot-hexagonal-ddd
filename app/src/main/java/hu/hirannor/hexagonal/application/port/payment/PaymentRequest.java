package hu.hirannor.hexagonal.application.port.payment;

import hu.hirannor.hexagonal.domain.core.valueobject.Money;
import hu.hirannor.hexagonal.domain.order.OrderId;
import hu.hirannor.hexagonal.domain.payment.PaymentMethod;
import hu.hirannor.hexagonal.infrastructure.command.CommandId;
import java.util.List;
import java.util.Objects;

public record PaymentRequest(
        CommandId id,
        OrderId orderId,
        List<PaymentItem> items,
        Money totalAmount,
        PaymentMethod method
) {

    public PaymentRequest {
        Objects.requireNonNull(id, "commandId must not be null");
        Objects.requireNonNull(orderId, "orderId must not be null");
        Objects.requireNonNull(totalAmount, "totalAmount must not be null");
        Objects.requireNonNull(method, "PaymentMethod must not be null");
    }

    public static PaymentRequest create(
            final OrderId orderId,
            final List<PaymentItem> items,
            final Money totalAmount,
            final PaymentMethod method
    ) {
        return new PaymentRequest(CommandId.generate(), orderId, items, totalAmount, method);
    }
}
