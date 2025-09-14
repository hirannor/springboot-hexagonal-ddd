package hu.hirannor.hexagonal.adapter.persistence.jpa.order.mapping;

import hu.hirannor.hexagonal.adapter.persistence.jpa.PaymentTransactionModel;
import hu.hirannor.hexagonal.adapter.persistence.jpa.order.OrderModel;
import hu.hirannor.hexagonal.adapter.persistence.jpa.order.OrderStatusModel;
import hu.hirannor.hexagonal.adapter.persistence.jpa.order.OrderedProductModel;
import hu.hirannor.hexagonal.domain.CustomerId;
import hu.hirannor.hexagonal.domain.order.Order;
import hu.hirannor.hexagonal.domain.order.OrderId;
import hu.hirannor.hexagonal.domain.order.OrderStatus;
import hu.hirannor.hexagonal.domain.order.OrderedProduct;
import hu.hirannor.hexagonal.domain.order.payment.PaymentTransaction;

import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class OrderModelToDomainMapper implements Function<OrderModel, Order> {

    private final Function<OrderStatusModel, OrderStatus> mapStatusModelToDomain;
    private final Function<OrderedProductModel, OrderedProduct> mapOrderedProductModelToDomain;
    private final Function<PaymentTransactionModel, PaymentTransaction> mapPaymentTransactionModelToDomain;

    public OrderModelToDomainMapper() {
        this.mapStatusModelToDomain = new OrderStatusModelToDomainMapper();
        this.mapOrderedProductModelToDomain = new OrderedProductModelToDomainMapper();
        this.mapPaymentTransactionModelToDomain = new PaymentTransactionModelToDomainMapper();
    }

    @Override
    public Order apply(final OrderModel model) {
        if (model == null) return null;

        final OrderStatus status = mapStatusModelToDomain.apply(model.getStatus());
        final Set<OrderedProduct> orderedProducts = model.getProducts()
                .stream()
                .map(mapOrderedProductModelToDomain)
                .collect(Collectors.toSet());

        final List<PaymentTransaction> transactions = model.getTransactions()
                .stream()
                .map(mapPaymentTransactionModelToDomain)
                .toList();

        return Order
                .empty()
                .id(OrderId.from(model.getOrderId()))
                .customer(CustomerId.from(model.getCustomerId()))
                .status(status)
                .orderedProducts(orderedProducts)
                .payments(transactions)
                .assemble();

    }
}
