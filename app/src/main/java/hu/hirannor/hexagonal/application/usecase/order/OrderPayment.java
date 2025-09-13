package hu.hirannor.hexagonal.application.usecase.order;

import hu.hirannor.hexagonal.domain.order.command.PayOrder;

public interface OrderPayment {
    void pay(PayOrder order);
}
