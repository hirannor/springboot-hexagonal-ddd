package io.github.hirannor.oms.domain.basket.command;

import io.github.hirannor.oms.domain.core.valueobject.CustomerId;
import io.github.hirannor.oms.infrastructure.command.Command;
import io.github.hirannor.oms.infrastructure.command.CommandId;

import java.util.Objects;

public record CheckoutBasket(
        CommandId id,
        CustomerId customerId
) implements Command {

    public CheckoutBasket {
        Objects.requireNonNull(id, "CommandId cannot be null");
        Objects.requireNonNull(customerId, "CustomerId cannot be null");
    }

    public static CheckoutBasket issue(final CustomerId customerId) {
        Objects.requireNonNull(customerId, "CustomerId cannot be null");
        return new CheckoutBasket(CommandId.generate(), customerId);
    }
}
