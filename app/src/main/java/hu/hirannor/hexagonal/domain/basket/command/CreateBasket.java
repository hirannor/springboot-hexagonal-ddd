package hu.hirannor.hexagonal.domain.basket.command;

import hu.hirannor.hexagonal.domain.CustomerId;
import hu.hirannor.hexagonal.domain.basket.BasketId;
import hu.hirannor.hexagonal.infrastructure.command.Command;
import hu.hirannor.hexagonal.infrastructure.command.CommandId;

public record CreateBasket(
        CommandId id,
        BasketId basketId,
        CustomerId customerId) implements Command {

    public static CreateBasket issue(final BasketId basket, final CustomerId customer) {
        return new CreateBasket(CommandId.generate(), basket, customer);
    }
}
