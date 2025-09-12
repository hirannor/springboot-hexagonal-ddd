package hu.hirannor.hexagonal.domain.order;

import hu.hirannor.hexagonal.domain.customer.CustomerId;
import hu.hirannor.hexagonal.domain.product.Product;
import hu.hirannor.hexagonal.infrastructure.command.Command;
import hu.hirannor.hexagonal.infrastructure.command.CommandId;

import java.util.Set;

public record MakeOrder(CommandId id, CustomerId customer, Set<Product> products) implements Command {

    public static MakeOrder issue(final CustomerId customer, final Set<Product> orderedProducts ) {
        return new MakeOrder(CommandId.generate(), customer,orderedProducts);
    }
}
