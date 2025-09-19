package hu.hirannor.hexagonal.domain.basket.command;

import hu.hirannor.hexagonal.domain.basket.BasketId;
import hu.hirannor.hexagonal.domain.core.valueobject.CustomerId;
import hu.hirannor.hexagonal.infrastructure.command.Command;
import hu.hirannor.hexagonal.infrastructure.command.CommandId;

public record CreateBasket(
        CommandId id,
        BasketId basketId,
        CustomerId customerId) implements Command {

    public static CreateBasket issue(final CustomerId customer) {
        return new CreateBasket(CommandId.generate(), BasketId.generate(), customer);
    }
}
