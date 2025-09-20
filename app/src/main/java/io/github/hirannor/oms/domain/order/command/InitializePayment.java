package io.github.hirannor.oms.domain.order.command;

import io.github.hirannor.oms.domain.order.OrderId;
import io.github.hirannor.oms.infrastructure.command.Command;
import io.github.hirannor.oms.infrastructure.command.CommandId;

public record InitializePayment(
        CommandId id,
        OrderId orderId
) implements Command {

    public static InitializePayment issue(final OrderId orderId) {
        return new InitializePayment(CommandId.generate(), orderId);
    }
}