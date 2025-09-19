package hu.hirannor.hexagonal.adapter.web.rest.order.mapping;

import hu.hirannor.hexagonal.adapter.web.rest.orders.model.PayOrderModel;
import hu.hirannor.hexagonal.domain.order.OrderId;
import hu.hirannor.hexagonal.domain.order.command.InitializePayment;
import java.util.function.Function;

public class CreatePayOrderModelToCommandMapper implements Function<PayOrderModel, InitializePayment> {

    public CreatePayOrderModelToCommandMapper() {}

    @Override
    public InitializePayment apply(final PayOrderModel model) {
        if (model == null) return null;

        return InitializePayment.issue(OrderId.from(model.getOrderId()));
    }
}
