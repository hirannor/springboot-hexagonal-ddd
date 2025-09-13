package hu.hirannor.hexagonal.application.usecase.order;

import hu.hirannor.hexagonal.domain.order.OrderId;
import hu.hirannor.hexagonal.domain.order.OrderStatus;
import hu.hirannor.hexagonal.infrastructure.command.Command;
import hu.hirannor.hexagonal.infrastructure.command.CommandId;

public record ChangeOrderStatus(CommandId id, OrderId orderId, OrderStatus status) implements Command {

    public static ChangeOrderStatus issue(final OrderId orderId, final OrderStatus status) {
        return new ChangeOrderStatus(CommandId.generate(), orderId, status);
    }
}
