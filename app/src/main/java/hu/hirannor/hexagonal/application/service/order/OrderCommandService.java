package hu.hirannor.hexagonal.application.service.order;

import hu.hirannor.hexagonal.application.port.payment.PaymentGateway;
import hu.hirannor.hexagonal.application.port.payment.PaymentItem;
import hu.hirannor.hexagonal.application.port.payment.PaymentRequest;
import hu.hirannor.hexagonal.application.usecase.order.ChangeOrderStatus;
import hu.hirannor.hexagonal.application.usecase.order.OrderCreation;
import hu.hirannor.hexagonal.application.usecase.order.OrderPaymentInitialization;
import hu.hirannor.hexagonal.application.usecase.order.OrderStatusChanging;
import hu.hirannor.hexagonal.domain.order.Order;
import hu.hirannor.hexagonal.domain.order.OrderId;
import hu.hirannor.hexagonal.domain.order.OrderRepository;
import hu.hirannor.hexagonal.domain.order.OrderItem;
import hu.hirannor.hexagonal.domain.order.command.CreateOrder;
import hu.hirannor.hexagonal.domain.order.command.InitializePayment;
import hu.hirannor.hexagonal.domain.order.command.PaymentInstruction;
import hu.hirannor.hexagonal.domain.order.payment.PaymentMethod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;


@Service
@Transactional(
    propagation = Propagation.REQUIRES_NEW,
    isolation = Isolation.REPEATABLE_READ
)
class OrderCommandService implements
        OrderCreation,
        OrderPaymentInitialization,
        OrderStatusChanging {

    private static final Logger LOGGER = LogManager.getLogger(
        OrderCommandService.class
    );

    private final Function<OrderItem, PaymentItem> mapOrderedProductToPaymentItem;

    private final OrderRepository orders;
    private final PaymentGateway payment;

    @Autowired
    OrderCommandService(final OrderRepository orders, final PaymentGateway payment) {
      this(orders, payment, new OrderedProductToPaymentItemMapper());
    }

    OrderCommandService(final OrderRepository orders,
                        final PaymentGateway payment,
                        final Function<OrderItem, PaymentItem> mapOrderedProductToPaymentItem) {
        this.orders = orders;
        this.payment = payment;
        this.mapOrderedProductToPaymentItem = mapOrderedProductToPaymentItem;
    }

    @Override
    public Order create(final CreateOrder create) {
        if (create == null) throw new IllegalArgumentException("CreateOrder is null");

        LOGGER.info("Start creating order with id :{} for customer id: {}",
            create.orderId().asText(),
            create.customer().asText());

        final Order order = Order.create(create);
        orders.save(order);

        LOGGER.info("Order with id: {} was successfully created for customer: {}",
            order.id().asText(),
            order.id().asText());

        return order;
    }

    public PaymentInstruction initialize(final InitializePayment command) {
        if (command == null) throw new IllegalArgumentException("InitializePayment is null");

        LOGGER.info("Start initialization of payment for order id: {}",
            command.orderId().asText()
        );

        final Order order = orders.findBy(command.orderId())
                .orElseThrow(failBecauseOrderWasNotFoundBy(command.orderId()));

        final List<PaymentItem> items = order.orderItems()
                .stream()
                .map(mapOrderedProductToPaymentItem)
                .toList();

        final PaymentInstruction instruction = payment.initialize(PaymentRequest.create(
            order.id(),
            items,
            order.totalPrice(),
            PaymentMethod.CARD
        ));

        order.initializePayment();
        orders.save(order);

        LOGGER.info("Payment initialization for order id: {} was successful",
            command.orderId().asText()
        );

        return instruction;
    }

    @Override
    public void change(final ChangeOrderStatus changeStatus) {
        if (changeStatus == null) throw new IllegalArgumentException("ChangeOrderStatus is null");

        LOGGER.info("Changing order status to: {} for order id: {}",
            changeStatus.status(),
            changeStatus.orderId().asText()
        );

        final Order order = orders.findBy(changeStatus.orderId())
                .orElseThrow(failBecauseOrderWasNotFoundBy(changeStatus.orderId()));

        order.changeStatus(changeStatus.status());
        orders.save(order);

        LOGGER.info("Order status is successfully changed for order id: {}",
            changeStatus.orderId().asText()
        );
    }

    private Supplier<IllegalStateException> failBecauseOrderWasNotFoundBy(OrderId order) {
        return () -> new IllegalStateException("Order not found with id " + order);
    }

}
