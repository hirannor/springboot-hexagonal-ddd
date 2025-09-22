package io.github.hirannor.oms.adapter.messaging.eventbus.message.mapping;

import io.github.hirannor.oms.adapter.messaging.eventbus.message.DomainEventModel;
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
import io.github.hirannor.oms.domain.order.events.OrderCreated;
import io.github.hirannor.oms.domain.order.events.OrderPaid;
import io.github.hirannor.oms.domain.order.events.OrderProcessing;
import io.github.hirannor.oms.domain.order.events.OrderShipped;
import io.github.hirannor.oms.domain.payment.events.PaymentCanceled;
import io.github.hirannor.oms.domain.payment.events.PaymentFailed;
import io.github.hirannor.oms.domain.payment.events.PaymentSucceeded;
import io.github.hirannor.oms.infrastructure.event.DomainEvent;

import java.util.Objects;
import java.util.function.Function;

public class DomainEventToModelMapper implements Function<DomainEvent, DomainEventModel> {

    private final Function<OrderCreated, OrderCreatedModel> mapOrderCreatedToModel;
    private final Function<PaymentSucceeded, PaymentSucceededModel> mapPaymentSucceededToModel;
    private final Function<PaymentFailed, PaymentFailedModel> mapPaymentFailedToModel;
    private final Function<PaymentCanceled, PaymentCanceledModel> mapPaymentCanceledToModel;
    private final Function<OrderPaid, OrderPaidModel> mapOrderPaidToModel;
    private final Function<OrderProcessing, OrderProcessingModel> mapOrderProcessingToModel;
    private final Function<OrderShipped, OrderShippedModel> mapOrderShippedToModel;

    public DomainEventToModelMapper() {
        this.mapOrderCreatedToModel = new OrderCreatedToModelMapper();
        this.mapPaymentSucceededToModel = new PaymentSucceededToModelMapper();
        this.mapPaymentFailedToModel = new PaymentFailedToModelMapper();
        this.mapPaymentCanceledToModel = new PaymentCanceledToModelMapper();
        this.mapOrderPaidToModel = new OrderPaidToModelMapper();
        this.mapOrderProcessingToModel = new OrderProcessingToModelMapper();
        this.mapOrderShippedToModel = new OrderShippedToModelMapper();
    }

    @Override
    public DomainEventModel apply(final DomainEvent domainEvent) {
        Objects.requireNonNull(domainEvent, "DomainEvent cannot be null");

        return switch (domainEvent) {
            case PaymentSucceeded evt -> mapPaymentSucceededToModel.apply(evt);
            case OrderCreated evt -> mapOrderCreatedToModel.apply(evt);
            case PaymentCanceled evt -> mapPaymentCanceledToModel.apply(evt);
            case PaymentFailed evt -> mapPaymentFailedToModel.apply(evt);
            case OrderPaid evt -> mapOrderPaidToModel.apply(evt);
            case OrderProcessing evt -> mapOrderProcessingToModel.apply(evt);
            case OrderShipped evt -> mapOrderShippedToModel.apply(evt);
            default -> null;
        };
    }
}
