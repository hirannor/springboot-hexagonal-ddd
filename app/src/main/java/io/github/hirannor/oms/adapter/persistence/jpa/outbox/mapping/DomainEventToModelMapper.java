package io.github.hirannor.oms.adapter.persistence.jpa.outbox.mapping;

import io.github.hirannor.oms.adapter.persistence.jpa.CurrencyModel;
import io.github.hirannor.oms.adapter.persistence.jpa.CurrencyToModelMapper;
import io.github.hirannor.oms.adapter.persistence.jpa.outbox.*;
import io.github.hirannor.oms.adapter.persistence.jpa.outbox.order.OrderCreatedModel;
import io.github.hirannor.oms.adapter.persistence.jpa.outbox.order.OrderPaidModel;
import io.github.hirannor.oms.adapter.persistence.jpa.outbox.order.OrderProcessingModel;
import io.github.hirannor.oms.adapter.persistence.jpa.outbox.order.OrderShippedModel;
import io.github.hirannor.oms.adapter.persistence.jpa.outbox.order.mapping.OrderCreatedToModelMapper;
import io.github.hirannor.oms.adapter.persistence.jpa.outbox.order.mapping.OrderPaidToModelMapper;
import io.github.hirannor.oms.adapter.persistence.jpa.outbox.order.mapping.OrderProcessingToModelMapper;
import io.github.hirannor.oms.adapter.persistence.jpa.outbox.order.mapping.OrderShippedToModelMapper;
import io.github.hirannor.oms.adapter.persistence.jpa.outbox.payment.PaymentCanceledModel;
import io.github.hirannor.oms.adapter.persistence.jpa.outbox.payment.PaymentFailedModel;
import io.github.hirannor.oms.adapter.persistence.jpa.outbox.payment.PaymentSucceededModel;
import io.github.hirannor.oms.adapter.persistence.jpa.outbox.payment.mapping.PaymentCanceledToModelMapper;
import io.github.hirannor.oms.adapter.persistence.jpa.outbox.payment.mapping.PaymentFailedToModelMapper;
import io.github.hirannor.oms.adapter.persistence.jpa.outbox.payment.mapping.PaymentSucceededToModelMapper;
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
        if (domainEvent == null) return null;

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
