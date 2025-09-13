package hu.hirannor.hexagonal.domain.basket.command;

import hu.hirannor.hexagonal.domain.CustomerId;
import hu.hirannor.hexagonal.domain.order.OrderedProduct;
import hu.hirannor.hexagonal.infrastructure.command.Command;
import hu.hirannor.hexagonal.infrastructure.command.CommandId;

import java.util.Objects;

public record AddProductToBasket(
        CommandId id,
        CustomerId customerId,
        OrderedProduct product
) implements Command {

    public AddProductToBasket {
        Objects.requireNonNull(id, "commandId cannot be null");
        Objects.requireNonNull(customerId, "customerId cannot be null");
        Objects.requireNonNull(product, "product cannot be null");
    }

    public static AddProductToBasket issue(final CustomerId customerId, final OrderedProduct product) {
        return new AddProductToBasket(CommandId.generate(), customerId, product);
    }
}
