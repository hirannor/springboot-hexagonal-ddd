package hu.hirannor.hexagonal.adapter.web.rest.order;

import hu.hirannor.hexagonal.adapter.web.rest.orders.api.OrdersApi;
import hu.hirannor.hexagonal.adapter.web.rest.orders.model.*;
import hu.hirannor.hexagonal.application.usecase.order.OrderCreation;
import hu.hirannor.hexagonal.application.usecase.order.OrderDisplaying;
import hu.hirannor.hexagonal.application.usecase.order.OrderPaymentInitialization;
import hu.hirannor.hexagonal.application.usecase.order.OrderStatusChanging;
import hu.hirannor.hexagonal.domain.order.Order;
import hu.hirannor.hexagonal.domain.order.OrderId;
import hu.hirannor.hexagonal.domain.order.command.CreateOrder;
import hu.hirannor.hexagonal.domain.order.command.PayOrder;
import hu.hirannor.hexagonal.infrastructure.adapter.DriverAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;

@RestController
@RequestMapping("/api")
@DriverAdapter
class OrderController implements OrdersApi {

    private final Function<CreateOrderModel, CreateOrder> mapCreateOrderModelToCommand;
    private final Function<Order, OrderModel> mapOrderToModel;

    private final OrderCreation orderCreation;
    private final OrderPaymentInitialization orderPayment;
    private final OrderDisplaying orders;
    private final OrderStatusChanging status;

    @Autowired
    OrderController(final OrderCreation orderCreation,
                    final OrderPaymentInitialization orderPayment,
                    final OrderDisplaying orders,
                    final OrderStatusChanging status) {
        this(
            orderCreation,
            orderPayment,
            orders,
            status,
            new CreateOrderModelToCommandMapper(),
            new OrderToModelMapper()
        );
    }

    OrderController(final OrderCreation orderCreation,
                    final OrderPaymentInitialization orderPayment,
                    final OrderDisplaying orders,
                    final OrderStatusChanging status,
                    final Function<CreateOrderModel, CreateOrder> mapCreateOrderModelToCommand,
                    final Function<Order, OrderModel> mapOrderToModel) {
        this.orderCreation = orderCreation;
        this.orderPayment = orderPayment;
        this.orders = orders;
        this.status = status;
        this.mapCreateOrderModelToCommand = mapCreateOrderModelToCommand;
        this.mapOrderToModel = mapOrderToModel;
    }

    @Override
    @PreAuthorize("hasRole('Customer')")
    public ResponseEntity<OrderModel> createOrder(final CreateOrderModel model) {
        final CreateOrder command = mapCreateOrderModelToCommand.apply(model);
        final Order order = orderCreation.create(command);

        final URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(order.id().value())
                .toUri();

        return ResponseEntity.created(location)
                .body(mapOrderToModel.apply(order));
    }

    @Override
    @PreAuthorize("hasRole('Customer')")
    public ResponseEntity<PayOrderResponseModel> pay(final String orderId, final PayOrderModel model) {
        final Function<PayOrderModel, PayOrder> mapPayOrderModelToCommand = new CreatePayOrderModelToCommandMapper(orderId);
        final PayOrder command = mapPayOrderModelToCommand.apply(model);

        orderPayment.initPay(command);

        return ResponseEntity.ok().build();
    }

    @Override
    @PreAuthorize("hasRole('Customer')")
    public ResponseEntity<OrderModel> displayBy(final String orderId) {
        return orders.displayBy(OrderId.from(orderId))
                .map(mapOrderToModel.andThen(ResponseEntity::ok))
                .orElse(ResponseEntity.notFound().build());
    }

    @Override
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<List<OrderModel>> displayAll() {
        final List<OrderModel> list = orders.displayAll()
                .stream()
                .map(mapOrderToModel)
                .toList();
        return ResponseEntity.ok(list);
    }

    @Override
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<Void> change(final String orderId, final ChangeOrderStatusModel changeOrderStatusModel) {
        return OrdersApi.super.change(orderId, changeOrderStatusModel);
    }

    @Override
    @PreAuthorize("hasAnyRole('Customer','Admin')")
    public ResponseEntity<Void> cancel(final String orderId, final CancelOrderModel cancelOrderModel) {
        return OrdersApi.super.cancel(orderId, cancelOrderModel);
    }

    private Supplier<IllegalStateException> failBecauseOrderWasNotFoundBy(final UUID orderId) {
        return () -> new IllegalStateException("Order not found by id " + orderId);
    }
}
