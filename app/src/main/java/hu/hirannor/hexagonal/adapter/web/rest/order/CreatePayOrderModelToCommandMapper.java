package hu.hirannor.hexagonal.adapter.web.rest.order;

import hu.hirannor.hexagonal.adapter.web.rest.orders.model.PayOrderModel;
import hu.hirannor.hexagonal.domain.order.OrderId;
import hu.hirannor.hexagonal.domain.order.command.PayOrder;

import java.util.function.Function;

class CreatePayOrderModelToCommandMapper implements Function<PayOrderModel, PayOrder> {

    CreatePayOrderModelToCommandMapper() {}

    @Override
    public PayOrder apply(final PayOrderModel model) {
        if (model == null) return null;

        return PayOrder.issue(OrderId.from(model.getOrderId()));
    }
}
