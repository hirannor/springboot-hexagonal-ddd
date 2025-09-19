package hu.hirannor.hexagonal.adapter.web.rest.order.mapping;

import hu.hirannor.hexagonal.adapter.web.rest.orders.model.ChangeOrderStatusModel;
import hu.hirannor.hexagonal.adapter.web.rest.orders.model.OrderStatusModel;
import hu.hirannor.hexagonal.application.usecase.order.ChangeOrderStatus;
import hu.hirannor.hexagonal.domain.order.OrderId;
import hu.hirannor.hexagonal.domain.order.OrderStatus;
import java.util.function.Function;

public class ChangeOrderStatusModelToCommandMapper implements Function<ChangeOrderStatusModel, ChangeOrderStatus> {

    private final Function<OrderStatusModel, OrderStatus> mapModelToDomain;

    public ChangeOrderStatusModelToCommandMapper() {
        this.mapModelToDomain = new OrderStatusModelToDomainMapper();
    }

    @Override
    public ChangeOrderStatus apply(final ChangeOrderStatusModel model) {
        if (model == null) return null;

        return ChangeOrderStatus.issue(
                OrderId.from(model.getOrderId()),
                mapModelToDomain.apply(model.getNewStatus())
        );

    }
}
