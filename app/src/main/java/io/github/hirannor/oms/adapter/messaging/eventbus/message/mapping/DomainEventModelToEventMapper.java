package io.github.hirannor.oms.adapter.messaging.eventbus.message.mapping;

import io.github.hirannor.oms.adapter.messaging.eventbus.message.DomainEventModel;
import io.github.hirannor.oms.adapter.messaging.eventbus.message.basket.BasketCheckedOutModel;
import io.github.hirannor.oms.adapter.messaging.eventbus.message.basket.mapping.BasketCheckedOutModelToDomainMapper;
import io.github.hirannor.oms.adapter.messaging.eventbus.message.inventory.StockDeductionFailedModel;
import io.github.hirannor.oms.adapter.messaging.eventbus.message.inventory.StockDeductionFailedModelToDomainMapper;
import io.github.hirannor.oms.adapter.messaging.eventbus.message.order.*;
import io.github.hirannor.oms.adapter.messaging.eventbus.message.order.mapping.*;
import io.github.hirannor.oms.adapter.messaging.eventbus.message.payment.PaymentCanceledModel;
import io.github.hirannor.oms.adapter.messaging.eventbus.message.payment.PaymentExpiredModel;
import io.github.hirannor.oms.adapter.messaging.eventbus.message.payment.PaymentFailedModel;
import io.github.hirannor.oms.adapter.messaging.eventbus.message.payment.PaymentSucceededModel;
import io.github.hirannor.oms.adapter.messaging.eventbus.message.payment.mapping.PaymentCanceledModelToDomainMapper;
import io.github.hirannor.oms.adapter.messaging.eventbus.message.payment.mapping.PaymentExpiredModelToDomainMapper;
import io.github.hirannor.oms.adapter.messaging.eventbus.message.payment.mapping.PaymentFailedModelToDomainMapper;
import io.github.hirannor.oms.adapter.messaging.eventbus.message.payment.mapping.PaymentSucceededModelToDomainMapper;
import io.github.hirannor.oms.domain.basket.events.BasketCheckedOut;
import io.github.hirannor.oms.domain.inventory.events.StockDeductionFailed;
import io.github.hirannor.oms.domain.order.events.*;
import io.github.hirannor.oms.domain.payment.events.PaymentCanceled;
import io.github.hirannor.oms.domain.payment.events.PaymentExpired;
import io.github.hirannor.oms.domain.payment.events.PaymentFailed;
import io.github.hirannor.oms.domain.payment.events.PaymentSucceeded;
import io.github.hirannor.oms.infrastructure.event.DomainEvent;

import java.util.function.Function;

public class DomainEventModelToEventMapper implements Function<DomainEventModel, DomainEvent> {

    private final Function<OrderCreatedModel, OrderCreated> mapOrderCreatedModelToDomain;
    private final Function<PaymentSucceededModel, PaymentSucceeded> mapPaymentSucceededModelToDomain;
    private final Function<PaymentFailedModel, PaymentFailed> mapPaymentFailedModelToDomain;
    private final Function<PaymentCanceledModel, PaymentCanceled> mapPaymentCanceledModelToDomain;
    private final Function<OrderPaidModel, OrderPaid> mapOrderPaidModelToDomain;
    private final Function<OrderPaymentFailedModel, OrderPaymentFailed> mapOrderPaymentFailedModelToDomain;
    private final Function<OrderProcessingModel, OrderProcessing> mapOrderProcessingModelToDomain;
    private final Function<OrderShippedModel, OrderShipped> mapOrderShippedModelToDomain;
    private final Function<BasketCheckedOutModel, BasketCheckedOut> mapBasketCheckedOutModelToDomain;
    private final Function<StockDeductionFailedModel, StockDeductionFailed> mapStockDeductionFailedModelToDomain;
    private final Function<PaymentExpiredModel, PaymentExpired> mapPaymentExpiredModelToDomain;
    private final Function<OrderPaymentExpiredModel, OrderPaymentExpired> mapOrderPaymentExpiredModelToDomain;

    public DomainEventModelToEventMapper() {
        this.mapOrderCreatedModelToDomain = new OrderCreatedModelToDomainMapper();
        this.mapPaymentSucceededModelToDomain = new PaymentSucceededModelToDomainMapper();
        this.mapPaymentFailedModelToDomain = new PaymentFailedModelToDomainMapper();
        this.mapPaymentCanceledModelToDomain = new PaymentCanceledModelToDomainMapper();
        this.mapOrderPaidModelToDomain = new OrderPaidModelToDomainMapper();
        this.mapOrderProcessingModelToDomain = new OrderProcessingModelToDomainMapper();
        this.mapOrderShippedModelToDomain = new OrderShippedModelToDomainMapper();
        this.mapBasketCheckedOutModelToDomain = new BasketCheckedOutModelToDomainMapper();
        this.mapOrderPaymentFailedModelToDomain = new OrderPaymentFailedModelToDomainMapper();
        this.mapStockDeductionFailedModelToDomain = new StockDeductionFailedModelToDomainMapper();
        this.mapPaymentExpiredModelToDomain = new PaymentExpiredModelToDomainMapper();
        this.mapOrderPaymentExpiredModelToDomain = new OrderPaymentExpiredModelToDomainMapper();
    }

    @Override
    public DomainEvent apply(final DomainEventModel evtModel) {
        if (evtModel == null) return null;

        return switch (evtModel) {
            case OrderCreatedModel model -> mapOrderCreatedModelToDomain.apply(model);
            case PaymentSucceededModel model -> mapPaymentSucceededModelToDomain.apply(model);
            case PaymentFailedModel model -> mapPaymentFailedModelToDomain.apply(model);
            case PaymentCanceledModel model -> mapPaymentCanceledModelToDomain.apply(model);
            case OrderPaidModel model -> mapOrderPaidModelToDomain.apply(model);
            case OrderProcessingModel model -> mapOrderProcessingModelToDomain.apply(model);
            case OrderShippedModel model -> mapOrderShippedModelToDomain.apply(model);
            case BasketCheckedOutModel model -> mapBasketCheckedOutModelToDomain.apply(model);
            case OrderPaymentFailedModel model -> mapOrderPaymentFailedModelToDomain.apply(model);
            case StockDeductionFailedModel model -> mapStockDeductionFailedModelToDomain.apply(model);
            case PaymentExpiredModel model -> mapPaymentExpiredModelToDomain.apply(model);
            case OrderPaymentExpiredModel model -> mapOrderPaymentExpiredModelToDomain.apply(model);
            default -> null;
        };
    }
}
