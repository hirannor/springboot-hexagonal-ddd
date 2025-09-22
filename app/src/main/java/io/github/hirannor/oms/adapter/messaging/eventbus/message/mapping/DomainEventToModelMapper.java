package io.github.hirannor.oms.adapter.messaging.eventbus.message.mapping;

import io.github.hirannor.oms.adapter.messaging.eventbus.message.DomainEventModel;
import io.github.hirannor.oms.adapter.messaging.eventbus.message.basket.BasketCheckedOutModel;
import io.github.hirannor.oms.adapter.messaging.eventbus.message.basket.mapping.BasketCheckedOutToModelMapper;
import io.github.hirannor.oms.adapter.messaging.eventbus.message.order.OrderCreatedModel;
import io.github.hirannor.oms.adapter.messaging.eventbus.message.order.OrderPaidModel;
import io.github.hirannor.oms.adapter.messaging.eventbus.message.order.OrderProcessingModel;
import io.github.hirannor.oms.adapter.messaging.eventbus.message.order.OrderShippedModel;
import io.github.hirannor.oms.adapter.messaging.eventbus.message.order.mapping.OrderCreatedToModelMapper;
import io.github.hirannor.oms.adapter.messaging.eventbus.message.order.mapping.OrderPaidToModelMapper;
import io.github.hirannor.oms.adapter.messaging.eventbus.message.order.mapping.OrderProcessingToModelMapper;
import io.github.hirannor.oms.adapter.messaging.eventbus.message.order.mapping.OrderShippedToModelMapper;
import io.github.hirannor.oms.adapter.messaging.eventbus.message.payment.PaymentCanceledModel;
import io.github.hirannor.oms.adapter.messaging.eventbus.message.payment.PaymentFailedModel;
import io.github.hirannor.oms.adapter.messaging.eventbus.message.payment.PaymentSucceededModel;
import io.github.hirannor.oms.adapter.messaging.eventbus.message.payment.mapping.PaymentCanceledToModelMapper;
import io.github.hirannor.oms.adapter.messaging.eventbus.message.payment.mapping.PaymentFailedToModelMapper;
import io.github.hirannor.oms.adapter.messaging.eventbus.message.payment.mapping.PaymentSucceededToModelMapper;
import io.github.hirannor.oms.domain.basket.events.BasketCheckedOut;
import io.github.hirannor.oms.domain.order.events.OrderCreated;
import io.github.hirannor.oms.domain.order.events.OrderPaid;
import io.github.hirannor.oms.domain.order.events.OrderProcessing;
import io.github.hirannor.oms.domain.order.events.OrderShipped;
import io.github.hirannor.oms.domain.payment.events.PaymentCanceled;
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

    public DomainEventToModelMapper() {
        this.mapOrderCreatedToModel = new OrderCreatedToModelMapper();
        this.mapPaymentSucceededToModel = new PaymentSucceededToModelMapper();
        this.mapPaymentFailedToModel = new PaymentFailedToModelMapper();
        this.mapPaymentCanceledToModel = new PaymentCanceledToModelMapper();
        this.mapOrderPaidToModel = new OrderPaidToModelMapper();
        this.mapOrderProcessingToModel = new OrderProcessingToModelMapper();
        this.mapOrderShippedToModel = new OrderShippedToModelMapper();
        this.mapBasketCheckedOutToModel = new BasketCheckedOutToModelMapper();
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
            default -> null;
        };
    }
}
