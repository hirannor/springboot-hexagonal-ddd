package hu.hirannor.hexagonal.domain.order.command;

import hu.hirannor.hexagonal.domain.Money;
import hu.hirannor.hexagonal.domain.order.OrderId;
import hu.hirannor.hexagonal.domain.product.ProductId;
import hu.hirannor.hexagonal.infrastructure.command.Command;
import hu.hirannor.hexagonal.infrastructure.command.CommandId;

import java.math.BigDecimal;

public record AddToCar(
        CommandId id,
        OrderId orderId,
        ProductId productId,
        int quantity,
        Money price) implements Command {

    public AddToCar {
        if (quantity() <= 0) throw new IllegalArgumentException("Quantity must be positive");
        if (price().amount().compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Price must be positive");
    }

    public static AddToCar issue(final OrderId orderId,
                                 final ProductId productId,
                                 final int quantity,
                                 final Money price) {
        return new AddToCar(CommandId.generate(), orderId, productId, quantity, price);
    }
}
