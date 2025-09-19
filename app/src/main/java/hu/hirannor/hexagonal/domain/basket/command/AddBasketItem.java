package hu.hirannor.hexagonal.domain.basket.command;

import hu.hirannor.hexagonal.domain.basket.BasketId;
import hu.hirannor.hexagonal.domain.basket.BasketItem;
import hu.hirannor.hexagonal.infrastructure.command.Command;
import hu.hirannor.hexagonal.infrastructure.command.CommandId;
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
