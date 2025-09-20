package io.github.hirannor.oms.application.port.payment;

import io.github.hirannor.oms.domain.core.valueobject.Money;
import io.github.hirannor.oms.domain.order.OrderId;
import io.github.hirannor.oms.domain.payment.PaymentMethod;
import io.github.hirannor.oms.infrastructure.command.CommandId;

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
