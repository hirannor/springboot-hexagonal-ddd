package hu.hirannor.hexagonal.application.service.order;

import hu.hirannor.hexagonal.application.port.payment.PaymentGateway;
import hu.hirannor.hexagonal.application.service.payment.PaymentReceipt;
import hu.hirannor.hexagonal.application.service.payment.ProcessPayment;
import hu.hirannor.hexagonal.application.usecase.order.ChangeOrderStatus;
import hu.hirannor.hexagonal.application.usecase.order.OrderCreation;
import hu.hirannor.hexagonal.application.usecase.order.OrderPayment;
import hu.hirannor.hexagonal.application.usecase.order.OrderStatusChanging;
import hu.hirannor.hexagonal.domain.order.Order;
import hu.hirannor.hexagonal.domain.order.OrderId;
import hu.hirannor.hexagonal.domain.order.OrderRepository;
import hu.hirannor.hexagonal.domain.order.command.MakeOrder;
import hu.hirannor.hexagonal.domain.order.command.PayOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Supplier;


@Service
@Transactional(
    propagation = Propagation.REQUIRES_NEW,
    isolation = Isolation.REPEATABLE_READ
)
class OrderCommandService implements
        OrderCreation,
        OrderPayment,
        OrderStatusChanging {

    private final OrderRepository orders;
    private final PaymentGateway payment;

    @Autowired
    OrderCommandService(final OrderRepository orders, final PaymentGateway payment) {
        this.orders = orders;
        this.payment = payment;
    }

    @Override
    public Order create(final MakeOrder create) {
        if (create == null) throw new IllegalArgumentException("create is null");

        final Order order = Order.create(create);
        orders.save(order);

        return order;
    }

    @Override
    public void pay(final PayOrder command) {
        if (command == null) throw new IllegalArgumentException("command is null");

        final Order order = orders.findBy(command.orderId())
                .orElseThrow(failBecauseOrderWasNotFoundBy(command.orderId()));

        final PaymentReceipt receipt = payment.process(ProcessPayment.create(
            order.id(),
            order.totalPrice(),
            "PAYMENT_METHOD"
        ));

        switch (receipt.status()) {
            case SUCCESS: {
                order.markAsPaid(order.customer());
                orders.save(order);
                break;
            }
            case PENDING, CANCELLED: break; // TODO
            case FAILURE: break; // TODO
        }
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
