package io.github.hirannor.oms.adapter.messaging.eventbus.message.mapping;

import io.github.hirannor.oms.adapter.messaging.eventbus.message.DomainEventModel;
import io.github.hirannor.oms.adapter.messaging.eventbus.message.basket.BasketCheckedOutModel;
import io.github.hirannor.oms.adapter.messaging.eventbus.message.basket.mapping.BasketCheckedOutToModelMapper;
import io.github.hirannor.oms.adapter.messaging.eventbus.message.inventory.StockDeductionFailedModel;
import io.github.hirannor.oms.adapter.messaging.eventbus.message.inventory.StockDeductionFailedToModelMapper;
import io.github.hirannor.oms.adapter.messaging.eventbus.message.order.*;
import io.github.hirannor.oms.adapter.messaging.eventbus.message.order.mapping.*;
import io.github.hirannor.oms.adapter.messaging.eventbus.message.payment.PaymentCanceledModel;
import io.github.hirannor.oms.adapter.messaging.eventbus.message.payment.PaymentExpiredModel;
import io.github.hirannor.oms.adapter.messaging.eventbus.message.payment.PaymentFailedModel;
import io.github.hirannor.oms.adapter.messaging.eventbus.message.payment.PaymentSucceededModel;
import io.github.hirannor.oms.adapter.messaging.eventbus.message.payment.mapping.PaymentCanceledToModelMapper;
import io.github.hirannor.oms.adapter.messaging.eventbus.message.payment.mapping.PaymentExpiredToModelMapper;
import io.github.hirannor.oms.adapter.messaging.eventbus.message.payment.mapping.PaymentFailedToModelMapper;
import io.github.hirannor.oms.adapter.messaging.eventbus.message.payment.mapping.PaymentSucceededToModelMapper;
import io.github.hirannor.oms.domain.basket.events.BasketCheckedOut;
import io.github.hirannor.oms.domain.inventory.events.StockDeductionFailed;
import io.github.hirannor.oms.domain.order.events.*;
import io.github.hirannor.oms.domain.payment.events.PaymentCanceled;
import io.github.hirannor.oms.domain.payment.events.PaymentExpired;
import io.github.hirannor.oms.domain.payment.events.PaymentFailed;
import io.github.hirannor.oms.domain.payment.events.PaymentSucceeded;
import io.github.hirannor.oms.infrastructure.event.DomainEvent;

import java.util.function.Function;

public class DomainEventToModelMapper implements Function<DomainEvent, DomainEventModel> {

    private final Function<OrderCreated, OrderCreatedModel> mapOrderCreatedToModel;
    private final Function<PaymentSucceeded, PaymentSucceededModel> mapPaymentSucceededToModel;
    private final Function<PaymentFailed, PaymentFailedModel> mapPaymentFailedToModel;
    private final Function<PaymentCanceled, PaymentCanceledModel> mapPaymentCanceledToModel;
    private final Function<OrderPaid, OrderPaidModel> mapOrderPaidToModel;
    private final Function<OrderProcessing, OrderProcessingModel> mapOrderProcessingToModel;
    private final Function<OrderShipped, OrderShippedModel> mapOrderShippedToModel;
    private final Function<BasketCheckedOut, BasketCheckedOutModel> mapBasketCheckedOutToModel;
    private final Function<OrderPaymentFailed, OrderPaymentFailedModel> mapOrderPaymentFailedToModel;
    private final Function<StockDeductionFailed, StockDeductionFailedModel> mapStockDeductionFailedToModel;
    private final Function<PaymentExpired, PaymentExpiredModel> mapPaymentExpiredToModel;
    private final Function<OrderPaymentExpired, OrderPaymentExpiredModel> mapOrderPaymentExpiredToModel;

    public DomainEventToModelMapper() {
        this.mapOrderCreatedToModel = new OrderCreatedToModelMapper();
        this.mapPaymentSucceededToModel = new PaymentSucceededToModelMapper();
        this.mapPaymentFailedToModel = new PaymentFailedToModelMapper();
        this.mapPaymentCanceledToModel = new PaymentCanceledToModelMapper();
        this.mapOrderPaidToModel = new OrderPaidToModelMapper();
        this.mapOrderProcessingToModel = new OrderProcessingToModelMapper();
        this.mapOrderShippedToModel = new OrderShippedToModelMapper();
        this.mapBasketCheckedOutToModel = new BasketCheckedOutToModelMapper();
        this.mapOrderPaymentFailedToModel = new OrderPaymentFailedToModelMapper();
        this.mapStockDeductionFailedToModel = new StockDeductionFailedToModelMapper();
        this.mapPaymentExpiredToModel = new PaymentExpiredToModelMapper();
        this.mapOrderPaymentExpiredToModel = new OrderPaymentExpiredToModelMapper();
    }

    @Override
    public DomainEventModel apply(final DomainEvent domainEvent) {
        if (domainEvent == null) return null;

        return switch (domainEvent) {
            case PaymentSucceeded evt -> mapPaymentSucceededToModel.apply(evt);
            case OrderCreated evt -> mapOrderCreatedToModel.apply(evt);
            case PaymentCanceled evt -> mapPaymentCanceledToModel.apply(evt);
            case PaymentFailed evt -> mapPaymentFailedToModel.apply(evt);
            case OrderPaid evt -> mapOrderPaidToModel.apply(evt);
            case OrderProcessing evt -> mapOrderProcessingToModel.apply(evt);
            case OrderShipped evt -> mapOrderShippedToModel.apply(evt);
            case BasketCheckedOut evt -> mapBasketCheckedOutToModel.apply(evt);
            case OrderPaymentFailed evt -> mapOrderPaymentFailedToModel.apply(evt);
            case StockDeductionFailed evt -> mapStockDeductionFailedToModel.apply(evt);
            case PaymentExpired evt -> mapPaymentExpiredToModel.apply(evt);
            case OrderPaymentExpired evt -> mapOrderPaymentExpiredToModel.apply(evt);
            default -> null;
        };
    }
}
