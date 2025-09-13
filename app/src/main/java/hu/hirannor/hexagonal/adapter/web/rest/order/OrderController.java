package hu.hirannor.hexagonal.adapter.web.rest.order;

import hu.hirannor.hexagonal.adapter.web.rest.orders.api.OrdersApi;
import hu.hirannor.hexagonal.adapter.web.rest.orders.model.*;
import hu.hirannor.hexagonal.application.usecase.order.OrderCreation;
import hu.hirannor.hexagonal.application.usecase.order.OrderDisplaying;
import hu.hirannor.hexagonal.application.usecase.order.OrderPayment;
import hu.hirannor.hexagonal.domain.order.Order;
import hu.hirannor.hexagonal.domain.order.OrderId;
import hu.hirannor.hexagonal.domain.order.command.MakeOrder;
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

    private final Function<CreateOrderModel, MakeOrder> mapCreateOrderModelToCommand;
    private final Function<Order, OrderModel> mapOrderToModel;

    private final OrderCreation orderCreation;
    private final OrderPayment orderPayment;
    private final OrderDisplaying orders;

    @Autowired
    OrderController(final OrderCreation orderCreation,
                    final OrderPayment orderPayment,
                    final OrderDisplaying orders) {
        this(
            orderCreation,
            orderPayment,
            orders,
            new CreateOrderModelToCommandMapper(),
            new OrderToModelMapper()
        );
    }

    OrderController(final OrderCreation orderCreation,
                    final OrderPayment orderPayment,
                    final OrderDisplaying orders,
                    final Function<CreateOrderModel, MakeOrder> mapCreateOrderModelToCommand,
                    final Function<Order, OrderModel> mapOrderToModel) {
        this.orderCreation = orderCreation;
        this.orderPayment = orderPayment;
        this.orders = orders;
        this.mapCreateOrderModelToCommand = mapCreateOrderModelToCommand;
        this.mapOrderToModel = mapOrderToModel;
    }

    @Override
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<OrderModel> createOrder(final CreateOrderModel model) {
        final MakeOrder command = mapCreateOrderModelToCommand.apply(model);
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
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<OrderModel> pay(final UUID orderId, final PayOrderModel model) {
        final Function<PayOrderModel, PayOrder> mapPayOrderModelToCommand = new CreatePayOrderModelToCommandMapper(orderId);
        final PayOrder command = mapPayOrderModelToCommand.apply(model);

        orderPayment.pay(command);

        final Order updated = orders.displayBy(OrderId.from(orderId.toString()))
                .orElseThrow(failBecauseOrderWasNotFoundBy(orderId));
        return ResponseEntity.ok(mapOrderToModel.apply(updated));
    }

    @Override
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<OrderModel> displayBy(final UUID orderId) {
        return orders.displayBy(OrderId.from(orderId.toString()))
                .map(mapOrderToModel.andThen(ResponseEntity::ok))
                .orElse(ResponseEntity.notFound().build());
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<OrderModel>> displayAll() {
        final List<OrderModel> list = orders.displayAll()
                .stream()
                .map(mapOrderToModel)
                .toList();
        return ResponseEntity.ok(list);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OrderModel> change(final UUID orderId, final ChangeOrderStatusModel changeOrderStatusModel) {
        return OrdersApi.super.change(orderId, changeOrderStatusModel);
    }

    @Override
    @PreAuthorize("hasAnyRole('CUSTOMER','ADMIN')")
    public ResponseEntity<OrderModel> cancel(final UUID orderId, final CancelOrderModel cancelOrderModel) {
        return OrdersApi.super.cancel(orderId, cancelOrderModel);
    }

    private Supplier<IllegalStateException> failBecauseOrderWasNotFoundBy(final UUID orderId) {
        return () -> new IllegalStateException("Order not found by id " + orderId);
    }
}
