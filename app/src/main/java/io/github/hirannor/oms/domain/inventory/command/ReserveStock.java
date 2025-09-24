package io.github.hirannor.oms.domain.inventory.command;

import io.github.hirannor.oms.domain.core.valueobject.ProductQuantity;
import io.github.hirannor.oms.domain.order.OrderId;
import io.github.hirannor.oms.infrastructure.command.Command;
import io.github.hirannor.oms.infrastructure.command.CommandId;

import java.util.List;

public record ReserveStock(CommandId id, OrderId orderId, List<ProductQuantity> products) implements Command {

    public static DeductStock issue(final OrderId orderId, final List<ProductQuantity> products) {
        return new DeductStock(CommandId.generate(), orderId, products);
    }
}
