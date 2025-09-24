package io.github.hirannor.oms.application.usecase.order;

import io.github.hirannor.oms.domain.core.valueobject.ProductQuantity;
import io.github.hirannor.oms.domain.order.OrderId;
import io.github.hirannor.oms.infrastructure.command.Command;
import io.github.hirannor.oms.infrastructure.command.CommandId;

import java.util.List;

public record StartOrderPaymentProcessing(CommandId id, OrderId orderId,
                                          List<ProductQuantity> products) implements Command {

    public static StartOrderPaymentProcessing issue(final OrderId orderId, final List<ProductQuantity> products) {
        return new StartOrderPaymentProcessing(CommandId.generate(), orderId, products);
    }
}
