package hu.hirannor.hexagonal.adapter.web.rest.order;

import hu.hirannor.hexagonal.adapter.web.rest.order.mapping.ChangeOrderStatusModelToCommandMapper;
import hu.hirannor.hexagonal.adapter.web.rest.order.mapping.CreateOrderModelToCommandMapper;
import hu.hirannor.hexagonal.adapter.web.rest.order.mapping.CreatePayOrderModelToCommandMapper;
import hu.hirannor.hexagonal.adapter.web.rest.order.mapping.OrderToModelMapper;
import hu.hirannor.hexagonal.adapter.web.rest.orders.api.OrdersApi;
import hu.hirannor.hexagonal.adapter.web.rest.orders.model.*;
import hu.hirannor.hexagonal.application.usecase.order.*;
import hu.hirannor.hexagonal.domain.order.Order;
import hu.hirannor.hexagonal.domain.order.OrderId;
import hu.hirannor.hexagonal.domain.order.command.CreateOrder;
import hu.hirannor.hexagonal.domain.order.command.InitializePayment;
import hu.hirannor.hexagonal.domain.order.command.PaymentInstruction;
import hu.hirannor.hexagonal.infrastructure.adapter.DriverAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.function.Function;

@RestController
@RequestMapping("/api")
@DriverAdapter
class OrderController implements OrdersApi {

    private final Function<CreateOrderModel, CreateOrder> mapCreateOrderModelToCommand;
    private final Function<Order, OrderModel> mapOrderToModel;
    private final Function<PayOrderModel, InitializePayment> mapPayOrderModelToCommand;
    private final Function<ChangeOrderStatusModel, ChangeOrderStatus> mapChangeOrderStatusModelToCommand;

    private final OrderCreation orderCreator;
    private final OrderPaymentInitialization payment;
    private final OrderDisplaying orders;
    private final OrderStatusChanging status;

    @Autowired
    OrderController(final OrderCreation orderCreator,
                    final OrderPaymentInitialization payment,
                    final OrderDisplaying orders,
                    final OrderStatusChanging status) {
        this(
            orderCreator,
            payment,
            orders,
            status,
            new CreateOrderModelToCommandMapper(),
            new OrderToModelMapper(),
            new CreatePayOrderModelToCommandMapper(),
            new ChangeOrderStatusModelToCommandMapper()
        );
    }

    OrderController(final OrderCreation orderCreator,
                    final OrderPaymentInitialization payment,
                    final OrderDisplaying orders,
                    final OrderStatusChanging status,
                    final Function<CreateOrderModel, CreateOrder> mapCreateOrderModelToCommand,
                    final Function<Order, OrderModel> mapOrderToModel,
                    final Function<PayOrderModel, InitializePayment> mapPayOrderModelToCommand,
                    final Function<ChangeOrderStatusModel, ChangeOrderStatus> mapChangeOrderStatusModelToCommand) {
        this.orderCreator = orderCreator;
        this.payment = payment;
        this.orders = orders;
        this.status = status;
        this.mapCreateOrderModelToCommand = mapCreateOrderModelToCommand;
        this.mapOrderToModel = mapOrderToModel;
        this.mapPayOrderModelToCommand = mapPayOrderModelToCommand;
        this.mapChangeOrderStatusModelToCommand = mapChangeOrderStatusModelToCommand;
    }

    @Override
    @PreAuthorize("hasRole('Customer')")
    public ResponseEntity<OrderModel> createOrder(final CreateOrderModel model) {
        final CreateOrder command = mapCreateOrderModelToCommand.apply(model);
        final Order order = orderCreator.create(command);

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
        final InitializePayment command = mapPayOrderModelToCommand.apply(model);

        final PaymentInstruction instruction = payment.initializeBy(command);

        return ResponseEntity.ok().body(
                new PayOrderResponseModel()
                        .paymentUrl(instruction.paymentUrl())
                        .orderId(instruction.orderId().asText())
        );
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
        final ChangeOrderStatus command = mapChangeOrderStatusModelToCommand.apply(changeOrderStatusModel);

        status.change(command);

        return ResponseEntity.noContent().build();
    }

    @Override
    @PreAuthorize("hasAnyRole('Customer','Admin')")
    public ResponseEntity<Void> cancel(final String orderId, final CancelOrderModel cancelOrderModel) {
        return ResponseEntity.noContent().build();
    }

}
