package hu.hirannor.hexagonal.application.usecase.order;

import hu.hirannor.hexagonal.domain.order.MakeOrder;

public interface OrderCreation {
    void create(MakeOrder command);
}
