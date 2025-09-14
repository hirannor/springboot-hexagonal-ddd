package hu.hirannor.hexagonal.adapter.web.rest.order.mapping;

import hu.hirannor.hexagonal.adapter.web.rest.orders.model.CreateOrderModel;
import hu.hirannor.hexagonal.adapter.web.rest.orders.model.OrderedProductModel;
import hu.hirannor.hexagonal.domain.CustomerId;
import hu.hirannor.hexagonal.domain.order.OrderedProduct;
import hu.hirannor.hexagonal.domain.order.command.CreateOrder;

import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CreateOrderModelToCommandMapper implements Function<CreateOrderModel, CreateOrder> {

    private final Function<OrderedProductModel, OrderedProduct> mapModelToDomain;

    public CreateOrderModelToCommandMapper() {
        this.mapModelToDomain = new OrderedProductModelToDomainMapper();
    }

    @Override
    public CreateOrder apply(final CreateOrderModel model) {
        if (model == null) return null;

        final Set<OrderedProduct> products = model.getProducts()
                .stream()
                .map(mapModelToDomain)
                .collect(Collectors.toSet());

        return CreateOrder.issue(
                CustomerId.from(model.getCustomerId()),
                products
        );
    }
}
