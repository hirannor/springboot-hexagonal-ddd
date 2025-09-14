package hu.hirannor.hexagonal.application.service.order;

import hu.hirannor.hexagonal.application.port.payment.PaymentGateway;
import hu.hirannor.hexagonal.application.port.payment.PaymentItem;
import hu.hirannor.hexagonal.application.port.payment.PaymentMethod;
import hu.hirannor.hexagonal.application.port.payment.ProcessPayment;
import hu.hirannor.hexagonal.application.usecase.order.ChangeOrderStatus;
import hu.hirannor.hexagonal.application.usecase.order.OrderCreation;
import hu.hirannor.hexagonal.application.usecase.order.OrderPaymentInitialization;
import hu.hirannor.hexagonal.application.usecase.order.OrderStatusChanging;
import hu.hirannor.hexagonal.domain.order.Order;
import hu.hirannor.hexagonal.domain.order.OrderId;
import hu.hirannor.hexagonal.domain.order.OrderRepository;
import hu.hirannor.hexagonal.domain.order.OrderedProduct;
import hu.hirannor.hexagonal.domain.order.command.CreateOrder;
import hu.hirannor.hexagonal.domain.order.command.PayOrder;
import hu.hirannor.hexagonal.domain.order.command.PaymentInstruction;
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

    private final Function<OrderedProduct, PaymentItem> mapOrderedProductToPaymentItem;

    private final OrderRepository orders;
    private final PaymentGateway payment;

    @Autowired
    OrderCommandService(final OrderRepository orders, final PaymentGateway payment) {
      this(orders, payment, new OrderedProductToPaymentItemMapper());
    }

    OrderCommandService(final OrderRepository orders,
                        final PaymentGateway payment,
                        final Function<OrderedProduct, PaymentItem> mapOrderedProductToPaymentItem) {
        this.orders = orders;
        this.payment = payment;
        this.mapOrderedProductToPaymentItem = mapOrderedProductToPaymentItem;
    }

    @Override
    public Order create(final CreateOrder create) {
        if (create == null) throw new IllegalArgumentException("create is null");

        final Order order = Order.create(create);
        orders.save(order);

        return order;
    }

    @Override
    public PaymentInstruction initPay(final PayOrder command) {
        if (command == null) throw new IllegalArgumentException("command is null");

        final Order order = orders.findBy(command.orderId())
                .orElseThrow(failBecauseOrderWasNotFoundBy(command.orderId()));

        final List<PaymentItem> items = order.products()
                .stream()
                .map(mapOrderedProductToPaymentItem)
                .toList();

        final PaymentInstruction instruction = payment.initialize(ProcessPayment.create(
            order.id(),
            items,
            order.totalPrice(),
            PaymentMethod.CARD
        ));

        order.initializePayment();
        orders.save(order);

        return instruction;
    }

    @Override
    public void change(final ChangeOrderStatus change) {
        if (change == null) throw new IllegalArgumentException("change is null");

        final Order order = orders.findBy(change.orderId())
                .orElseThrow(failBecauseOrderWasNotFoundBy(change.orderId()));

        order.changeStatus(change.status());
        orders.save(order);
    }

    private Supplier<IllegalStateException> failBecauseOrderWasNotFoundBy(OrderId order) {
        return () -> new IllegalStateException("Order not found with id " + order);
    }

}
