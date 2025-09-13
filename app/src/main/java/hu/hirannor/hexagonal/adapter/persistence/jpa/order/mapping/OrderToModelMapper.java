package hu.hirannor.hexagonal.adapter.persistence.jpa.order.mapping;

import hu.hirannor.hexagonal.adapter.persistence.jpa.CurrencyModel;
import hu.hirannor.hexagonal.adapter.persistence.jpa.CurrencyToModelMapper;
import hu.hirannor.hexagonal.adapter.persistence.jpa.order.OrderModel;
import hu.hirannor.hexagonal.adapter.persistence.jpa.order.OrderStatusModel;
import hu.hirannor.hexagonal.adapter.persistence.jpa.order.OrderedProductModel;
import hu.hirannor.hexagonal.domain.Currency;
import hu.hirannor.hexagonal.domain.order.Order;
import hu.hirannor.hexagonal.domain.order.OrderStatus;
import hu.hirannor.hexagonal.domain.order.OrderedProduct;

import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class OrderToModelMapper implements Function<Order, OrderModel> {

    private final Function<OrderStatus, OrderStatusModel> mapStatusToModel;
    private final Function<Currency, CurrencyModel> mapCurrencyToModel;
    private final Function<OrderedProduct, OrderedProductModel> mapOrderedProductToModel;


    public OrderToModelMapper() {
        this.mapStatusToModel = new OrderStatusToModelMapper();
        this.mapCurrencyToModel = new CurrencyToModelMapper();
        this.mapOrderedProductToModel = new OrderedProductToModelMapper();
    }

    @Override
    public OrderModel apply(final Order domain) {
        if (domain == null) return null;

        final OrderStatusModel status = mapStatusToModel.apply(domain.status());

        final OrderModel model = new OrderModel();
        model.setOrderId(domain.id().asText());
        model.setStatus(status);
        model.setCustomerId(model.getCustomerId());
        model.setCreatedAt(domain.createdAt());
        model.setTotalPriceAmount(domain.totalPrice().amount());
        model.setTotalPriceCurrency(mapCurrencyToModel.apply(domain.totalPrice().currency()));

        final Set<OrderedProductModel> orderedProducts = domain.products()
                .stream()
                .map(mapOrderedProductToModel)
                .collect(Collectors.toSet());

        model.setProducts(orderedProducts);

        return model;
    }
}
