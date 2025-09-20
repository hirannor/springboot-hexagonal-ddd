package io.github.hirannor.oms.domain.basket.command;

import io.github.hirannor.oms.domain.basket.BasketId;
import io.github.hirannor.oms.domain.basket.BasketItem;
import io.github.hirannor.oms.infrastructure.command.Command;
import io.github.hirannor.oms.infrastructure.command.CommandId;

import java.util.Objects;

public record AddBasketItem(
        CommandId id,
        BasketId basketId,
        BasketItem item
) implements Command {

    public AddBasketItem {
        Objects.requireNonNull(id, "commandId cannot be null");
        Objects.requireNonNull(basketId, "basketId cannot be null");
        Objects.requireNonNull(item, "item cannot be null");
    }

    public static AddBasketItem issue(final BasketId id, final BasketItem item) {
        return new AddBasketItem(CommandId.generate(), id, item);
    }
}
