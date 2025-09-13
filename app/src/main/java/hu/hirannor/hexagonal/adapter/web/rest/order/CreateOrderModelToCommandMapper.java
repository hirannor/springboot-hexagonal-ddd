package hu.hirannor.hexagonal.adapter.web.rest.order;

import hu.hirannor.hexagonal.adapter.web.rest.orders.model.CreateOrderModel;
import hu.hirannor.hexagonal.domain.order.command.MakeOrder;
import hu.hirannor.hexagonal.domain.product.CreateProduct;

import java.util.function.Function;

class CreateOrderModelToCommandMapper implements Function<CreateOrderModel, MakeOrder> {

    CreateOrderModelToCommandMapper() {}

    @Override
    public MakeOrder apply(CreateOrderModel model) {
        return null;
    }
}
