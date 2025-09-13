package hu.hirannor.hexagonal.application.usecase.order;

public interface OrderStatusChanging {
    void change(ChangeOrderStatus cmd);
}
