package io.github.hirannor.oms.domain.basket.command;

import io.github.hirannor.oms.domain.basket.BasketId;
import io.github.hirannor.oms.domain.core.valueobject.CustomerId;
import io.github.hirannor.oms.infrastructure.command.Command;
import io.github.hirannor.oms.infrastructure.command.CommandId;

public record CreateBasket(
        CommandId id,
        BasketId basketId,
        CustomerId customerId) implements Command {

    public static CreateBasket issue(final CustomerId customer) {
        return new CreateBasket(CommandId.generate(), BasketId.generate(), customer);
    }
}
