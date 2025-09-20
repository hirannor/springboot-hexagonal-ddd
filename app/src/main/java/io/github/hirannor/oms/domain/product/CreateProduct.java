package io.github.hirannor.oms.domain.product;

import io.github.hirannor.oms.domain.core.valueobject.Money;
import io.github.hirannor.oms.infrastructure.command.Command;
import io.github.hirannor.oms.infrastructure.command.CommandId;

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
