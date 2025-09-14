package hu.hirannor.hexagonal.adapter.web.rest.order;

import hu.hirannor.hexagonal.adapter.web.rest.orders.model.PayOrderModel;
import hu.hirannor.hexagonal.domain.order.command.PayOrder;

import java.util.function.Function;

class CreatePayOrderModelToCommandMapper implements Function<PayOrderModel, PayOrder> {

    CreatePayOrderModelToCommandMapper(final String orderId) {}

    @Override
    public PayOrder apply(PayOrderModel payOrderModel) {
        return null;
    }
}
