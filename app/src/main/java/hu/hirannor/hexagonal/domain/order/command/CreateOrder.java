package hu.hirannor.hexagonal.domain.order.command;

import hu.hirannor.hexagonal.domain.core.valueobject.CustomerId;
import hu.hirannor.hexagonal.domain.order.OrderId;
import hu.hirannor.hexagonal.domain.order.OrderItem;
import hu.hirannor.hexagonal.infrastructure.command.Command;
import hu.hirannor.hexagonal.infrastructure.command.CommandId;

import java.util.List;

public record CreateOrder(CommandId id, OrderId orderId, CustomerId customer, List<OrderItem> orderItems) implements Command {

    public static CreateOrder issue(final CustomerId customer, final List<OrderItem> orderItems) {
        return new CreateOrder(CommandId.generate(), OrderId.generate(), customer, orderItems);
    }
}
