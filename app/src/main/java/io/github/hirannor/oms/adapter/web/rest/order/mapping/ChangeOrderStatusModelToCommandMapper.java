package io.github.hirannor.oms.adapter.web.rest.order.mapping;

import io.github.hirannor.oms.adapter.web.rest.orders.model.ChangeOrderStatusModel;
import io.github.hirannor.oms.adapter.web.rest.orders.model.OrderStatusModel;
import io.github.hirannor.oms.application.usecase.order.ChangeOrderStatus;
import io.github.hirannor.oms.domain.order.OrderId;
import io.github.hirannor.oms.domain.order.OrderStatus;

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
