package io.github.hirannor.oms.adapter.web.rest.order.mapping;

import io.github.hirannor.oms.adapter.web.rest.orders.model.PayOrderModel;
import io.github.hirannor.oms.domain.order.OrderId;
import io.github.hirannor.oms.domain.order.command.InitializePayment;

import java.util.function.Function;

public class CreatePayOrderModelToCommandMapper implements Function<PayOrderModel, InitializePayment> {

    public CreatePayOrderModelToCommandMapper() {}

    @Override
    public InitializePayment apply(final PayOrderModel model) {
        if (model == null) return null;

        return InitializePayment.issue(OrderId.from(model.getOrderId()));
    }
}
