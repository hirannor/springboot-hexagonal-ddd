package io.github.hirannor.oms.application.usecase.order;

import io.github.hirannor.oms.domain.order.OrderId;
import io.github.hirannor.oms.domain.order.OrderStatus;
import io.github.hirannor.oms.infrastructure.command.Command;
import io.github.hirannor.oms.infrastructure.command.CommandId;

public record ChangeOrderStatus(CommandId id, OrderId orderId, OrderStatus status) implements Command {

    public static ChangeOrderStatus issue(final OrderId orderId, final OrderStatus status) {
        return new ChangeOrderStatus(CommandId.generate(), orderId, status);
    }
}
