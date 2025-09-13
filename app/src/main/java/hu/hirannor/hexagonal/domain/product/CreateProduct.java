package hu.hirannor.hexagonal.domain.product;

import hu.hirannor.hexagonal.domain.Money;
import hu.hirannor.hexagonal.infrastructure.command.Command;
import hu.hirannor.hexagonal.infrastructure.command.CommandId;

import java.math.BigDecimal;
import java.util.Objects;

public record CreateProduct(
        CommandId id,
        ProductId productId,
        String name,
        String description,
        Money price
) implements Command {

    public CreateProduct {
        Objects.requireNonNull(productId, "ProductId cannot be null");
        Objects.requireNonNull(name, "Product name cannot be null");
        Objects.requireNonNull(price, "Product price cannot be null");

        if (price.amount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price must be positive");
        }
    }

    public static CreateProduct issue(
            final String name,
            final String description,
            final Money price) {
        return new CreateProduct(CommandId.generate(), ProductId.generate(), name, description, price);
    }
}
