package io.github.hirannor.oms.adapter.persistence.jpa.outbox.mapping;

import io.github.hirannor.oms.adapter.persistence.jpa.outbox.DomainEventModel;
import io.github.hirannor.oms.adapter.persistence.jpa.outbox.basket.BasketCheckedOutModel;
import io.github.hirannor.oms.adapter.persistence.jpa.outbox.basket.mapping.BasketCheckedOutToModelMapper;
import io.github.hirannor.oms.adapter.persistence.jpa.outbox.inventory.StockDeductionFailedModel;
import io.github.hirannor.oms.adapter.persistence.jpa.outbox.inventory.StockDeductionFailedToModelMapper;
import io.github.hirannor.oms.adapter.persistence.jpa.outbox.order.*;
import io.github.hirannor.oms.adapter.persistence.jpa.outbox.order.mapping.*;
import io.github.hirannor.oms.adapter.persistence.jpa.outbox.payment.PaymentCanceledModel;
import io.github.hirannor.oms.adapter.persistence.jpa.outbox.payment.PaymentExpiredModel;
import io.github.hirannor.oms.adapter.persistence.jpa.outbox.payment.PaymentFailedModel;
import io.github.hirannor.oms.adapter.persistence.jpa.outbox.payment.PaymentSucceededModel;
import io.github.hirannor.oms.adapter.persistence.jpa.outbox.payment.mapping.PaymentCanceledToModelMapper;
import io.github.hirannor.oms.adapter.persistence.jpa.outbox.payment.mapping.PaymentExpiredToModelMapper;
import io.github.hirannor.oms.adapter.persistence.jpa.outbox.payment.mapping.PaymentFailedToModelMapper;
import io.github.hirannor.oms.adapter.persistence.jpa.outbox.payment.mapping.PaymentSucceededToModelMapper;
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
    private final Function<OrderPaymentExpired, OrderPaymentExpiredModel> mapOrderPaymentExpiredModelToDomain;
    private final Function<PaymentExpired, PaymentExpiredModel> mapPaymentExpiredToModel;

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
        this.mapOrderPaymentExpiredModelToDomain = new OrderPaymentExpiredToModelMapper();
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
            case OrderPaymentExpired evt -> mapOrderPaymentExpiredModelToDomain.apply(evt);
            default -> null;
        };
    }
}
