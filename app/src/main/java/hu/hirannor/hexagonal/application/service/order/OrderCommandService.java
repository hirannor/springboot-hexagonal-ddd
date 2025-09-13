package hu.hirannor.hexagonal.application.service.order;

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

    @Autowired
    OrderCommandService(final OrderRepository orders) {
        this.orders = orders;
    }

    @Override
    public Order create(final MakeOrder create) {
        if (create == null) throw new IllegalArgumentException("create is null");

        final Order order = Order.create(create);
        orders.save(order);

        return order;
    }

    @Override
    public void pay(final PayOrder payment) {
        if (payment == null) throw new IllegalArgumentException("payment is null");

        final Order order = orders.findBy(payment.orderId())
                .orElseThrow(failBecauseOrderWasNotFoundBy(payment.orderId()));

        order.pay(order.customer());

        orders.save(order);
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
