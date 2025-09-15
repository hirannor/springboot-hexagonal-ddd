package hu.hirannor.hexagonal.adapter.persistence.jpa.order.mapping;

import hu.hirannor.hexagonal.adapter.persistence.jpa.CurrencyModel;
import hu.hirannor.hexagonal.adapter.persistence.jpa.CurrencyToModelMapper;
import hu.hirannor.hexagonal.adapter.persistence.jpa.PaymentTransactionModel;
import hu.hirannor.hexagonal.adapter.persistence.jpa.order.OrderModel;
import hu.hirannor.hexagonal.adapter.persistence.jpa.order.OrderStatusModel;
import hu.hirannor.hexagonal.adapter.persistence.jpa.order.OrderedProductModel;
import hu.hirannor.hexagonal.domain.Currency;
import hu.hirannor.hexagonal.domain.order.Order;
import hu.hirannor.hexagonal.domain.order.OrderStatus;
import hu.hirannor.hexagonal.domain.order.OrderedProduct;
import hu.hirannor.hexagonal.domain.order.payment.PaymentTransaction;
import hu.hirannor.hexagonal.infrastructure.modelling.Modeller;

import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class OrderModeller implements Modeller<OrderModel> {

    private final Function<Currency, CurrencyModel> mapCurrencyToModel;
    private final Function<OrderStatus, OrderStatusModel> mapStatusToModel;
    private final Function<OrderedProduct, OrderedProductModel> mapOrderedProductToModel;

    private final Order domain;

    public OrderModeller(final Order domain) {
        this.domain = domain;
        this.mapCurrencyToModel = new CurrencyToModelMapper();
        this.mapStatusToModel = new OrderStatusToModelMapper();
        this.mapOrderedProductToModel = new OrderedProductToModelMapper();
    }

    public static OrderModeller applyChangesFrom(final Order domain) {
        return new OrderModeller(domain);
    }

    @Override
    public OrderModel to(final OrderModel from) {
        if (from == null) return null;

        from.setOrderId(domain.id().asText());
        from.setCustomerId(domain.customer().asText());
        from.setCreatedAt(domain.createdAt());
        from.setTotalPriceAmount(domain.totalPrice().amount());
        from.setTotalPriceCurrency(mapCurrencyToModel.apply(domain.totalPrice().currency()));
        from.setStatus(mapStatusToModel.apply(domain.status()));

        final Set<OrderedProductModel> products = domain.products()
                .stream()
                .map(mapOrderedProductToModel)
                .collect(Collectors.toSet());

        from.setProducts(products);

        final PaymentTransactionModel model = Optional.ofNullable(domain.transaction())
                .map(tx -> {
                    PaymentTransactionModel mapped = getMapped(from, tx);
                    mapped.setOrder(from);
                    return mapped;
                })
                .orElse(null);

        from.setTransaction(model);

        return from;
    }

    private static PaymentTransactionModel getMapped(OrderModel from, PaymentTransaction tx) {
        return PaymentTransactionModeller
                .applyChangesFrom(tx)
                .to(from.getTransaction());
    }
}
