package hu.hirannor.hexagonal.domain.order.command;

import hu.hirannor.hexagonal.domain.CustomerId;
import hu.hirannor.hexagonal.domain.order.OrderedProduct;
import hu.hirannor.hexagonal.infrastructure.command.Command;
import hu.hirannor.hexagonal.infrastructure.command.CommandId;

import java.util.Set;

public record MakeOrder(CommandId id, CustomerId customer, Set<OrderedProduct> products) implements Command {

    public static MakeOrder issue(final CustomerId customer, final Set<OrderedProduct> orderedProducts ) {
        return new MakeOrder(CommandId.generate(), customer,orderedProducts);
    }
}
