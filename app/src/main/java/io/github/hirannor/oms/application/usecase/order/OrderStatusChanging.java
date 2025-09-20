package io.github.hirannor.oms.application.usecase.order;

public interface OrderStatusChanging {
    void change(ChangeOrderStatus cmd);
}
