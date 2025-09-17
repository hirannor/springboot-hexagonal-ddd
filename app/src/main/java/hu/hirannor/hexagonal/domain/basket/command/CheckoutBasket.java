package hu.hirannor.hexagonal.domain.basket.command;

import hu.hirannor.hexagonal.domain.core.valueobject.CustomerId;
import hu.hirannor.hexagonal.infrastructure.command.Command;
import hu.hirannor.hexagonal.infrastructure.command.CommandId;

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
