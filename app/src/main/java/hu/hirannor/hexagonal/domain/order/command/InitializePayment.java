package hu.hirannor.hexagonal.domain.order.command;

import hu.hirannor.hexagonal.domain.order.OrderId;
import hu.hirannor.hexagonal.infrastructure.command.Command;
import hu.hirannor.hexagonal.infrastructure.command.CommandId;

public record InitializePayment(
        CommandId id,
        OrderId orderId
) implements Command {

    public static InitializePayment issue(final OrderId orderId) {
        return new InitializePayment(CommandId.generate(), orderId);
    }
}