package io.github.hirannor.oms.domain.order.command;

import io.github.hirannor.oms.domain.core.valueobject.CustomerId;
import io.github.hirannor.oms.domain.order.OrderId;
import io.github.hirannor.oms.domain.order.OrderItem;
import io.github.hirannor.oms.infrastructure.command.Command;
import io.github.hirannor.oms.infrastructure.command.CommandId;

import java.util.List;

public record CreateOrder(CommandId id, OrderId orderId, CustomerId customer,
                          List<OrderItem> orderItems) implements Command {

    public static CreateOrder issue(final CustomerId customer, final List<OrderItem> orderItems) {
        return new CreateOrder(CommandId.generate(), OrderId.generate(), customer, orderItems);
    }
}
