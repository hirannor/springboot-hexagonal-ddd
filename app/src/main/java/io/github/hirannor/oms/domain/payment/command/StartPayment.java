package io.github.hirannor.oms.domain.payment.command;

import io.github.hirannor.oms.domain.core.valueobject.Money;
import io.github.hirannor.oms.domain.order.OrderId;
import io.github.hirannor.oms.domain.payment.PaymentId;
import io.github.hirannor.oms.infrastructure.command.Command;
import io.github.hirannor.oms.infrastructure.command.CommandId;

import java.util.Objects;

public record StartPayment(
        CommandId id,
        PaymentId paymentId,
        OrderId orderId,
        Money amount,
        String providerReference
) implements Command {

    public StartPayment {
        Objects.requireNonNull(id, "CommandId cannot be null");
        Objects.requireNonNull(paymentId, "PaymentId cannot be null");
        Objects.requireNonNull(orderId, "OrderId cannot be null");
        Objects.requireNonNull(amount, "Amount cannot be null");
        Objects.requireNonNull(providerReference, "Provider reference cannot be null");
    }

    public static StartPayment issue(final OrderId orderId, final Money amount, final String providerReference) {
        return new StartPayment(
                CommandId.generate(),
                PaymentId.generate(),
                orderId,
                amount,
                providerReference
        );
    }
}
