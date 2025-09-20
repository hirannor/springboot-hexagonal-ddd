package io.github.hirannor.oms.adapter.web.rest.order;

import io.github.hirannor.oms.adapter.web.rest.order.mapping.ChangeOrderStatusModelToCommandMapper;
import io.github.hirannor.oms.adapter.web.rest.order.mapping.CreateOrderModelToCommandMapper;
import io.github.hirannor.oms.adapter.web.rest.order.mapping.OrderToModelMapper;
import io.github.hirannor.oms.adapter.web.rest.order.mapping.PaymentInstructionToPayOrderResponseModelMapper;
import io.github.hirannor.oms.adapter.web.rest.orders.api.OrdersApi;
import io.github.hirannor.oms.adapter.web.rest.orders.model.ChangeOrderStatusModel;
import io.github.hirannor.oms.adapter.web.rest.orders.model.CreateOrderModel;
import io.github.hirannor.oms.adapter.web.rest.orders.model.OrderModel;
import io.github.hirannor.oms.adapter.web.rest.orders.model.PayOrderResponseModel;
import io.github.hirannor.oms.application.usecase.order.*;
import io.github.hirannor.oms.application.usecase.payment.PaymentInitialization;
import io.github.hirannor.oms.domain.order.Order;
import io.github.hirannor.oms.domain.order.OrderId;
import io.github.hirannor.oms.domain.order.command.CreateOrder;
import io.github.hirannor.oms.domain.order.command.InitializePayment;
import io.github.hirannor.oms.domain.order.command.PaymentInstruction;
import io.github.hirannor.oms.infrastructure.adapter.DriverAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    private final Function<ChangeOrderStatusModel, ChangeOrderStatus> mapChangeOrderStatusModelToCommand;
    private final Function<PaymentInstruction, PayOrderResponseModel> mapPaymentInstructionToModel;

    private final OrderCreation orderCreator;
    private final PaymentInitialization payment;
    private final OrderDisplaying orders;
    private final OrderStatusChanging status;
    private final OrderCancellation order;

    @Autowired
    OrderController(final OrderCreation orderCreator,
                    final PaymentInitialization payment,
                    final OrderDisplaying orders,
                    final OrderStatusChanging status,
                    final OrderCancellation order) {
        this(
            orderCreator,
            payment,
            orders,
            status,
            order,
            new CreateOrderModelToCommandMapper(),
            new OrderToModelMapper(),
            new ChangeOrderStatusModelToCommandMapper(),
            new PaymentInstructionToPayOrderResponseModelMapper()
        );
    }

    OrderController(final OrderCreation orderCreator,
                    final PaymentInitialization payment,
                    final OrderDisplaying orders,
                    final OrderStatusChanging status,
                    final OrderCancellation order,
                    final Function<CreateOrderModel, CreateOrder> mapCreateOrderModelToCommand,
                    final Function<Order, OrderModel> mapOrderToModel,
                    final Function<ChangeOrderStatusModel, ChangeOrderStatus> mapChangeOrderStatusModelToCommand,
                    final Function<PaymentInstruction, PayOrderResponseModel> mapPaymentInstructionToModel) {
        this.orderCreator = orderCreator;
        this.payment = payment;
        this.orders = orders;
        this.status = status;
        this.order = order;
        this.mapCreateOrderModelToCommand = mapCreateOrderModelToCommand;
        this.mapOrderToModel = mapOrderToModel;
        this.mapChangeOrderStatusModelToCommand = mapChangeOrderStatusModelToCommand;
        this.mapPaymentInstructionToModel = mapPaymentInstructionToModel;
    }

    @Override
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
    public ResponseEntity<PayOrderResponseModel> pay(final String orderId) {
        final InitializePayment command = InitializePayment.issue(OrderId.from(orderId));
        final PaymentInstruction instruction = payment.initialize(command);

        return ResponseEntity.ok().body(mapPaymentInstructionToModel.apply(instruction));
    }

    @Override
    public ResponseEntity<OrderModel> displayBy(final String orderId) {
        return orders.displayBy(OrderId.from(orderId))
                .map(mapOrderToModel.andThen(ResponseEntity::ok))
                .orElse(ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<List<OrderModel>> displayAll() {
        final List<OrderModel> list = orders.displayAll()
                .stream()
                .map(mapOrderToModel)
                .toList();
        return ResponseEntity.ok(list);
    }

    @Override
    public ResponseEntity<Void> change(final String orderId, final ChangeOrderStatusModel changeOrderStatusModel) {
        final ChangeOrderStatus command = mapChangeOrderStatusModelToCommand.apply(changeOrderStatusModel);

        status.change(command);

        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> cancel(final String orderId) {
        order.cancelBy(OrderId.from(orderId));
        return ResponseEntity.noContent().build();
    }

}
