package io.github.hirannor.oms.adapter.persistence.jpa.outbox.mapping;

import io.github.hirannor.oms.adapter.persistence.jpa.outbox.*;
import io.github.hirannor.oms.adapter.persistence.jpa.outbox.order.OrderCreatedModel;
import io.github.hirannor.oms.adapter.persistence.jpa.outbox.order.OrderPaidModel;
import io.github.hirannor.oms.adapter.persistence.jpa.outbox.order.OrderProcessingModel;
import io.github.hirannor.oms.adapter.persistence.jpa.outbox.order.OrderShippedModel;
import io.github.hirannor.oms.adapter.persistence.jpa.outbox.order.mapping.OrderCreatedModelToDomainMapper;
import io.github.hirannor.oms.adapter.persistence.jpa.outbox.order.mapping.OrderPaidModelToDomainMapper;
import io.github.hirannor.oms.adapter.persistence.jpa.outbox.order.mapping.OrderProcessingModelToDomainMapper;
import io.github.hirannor.oms.adapter.persistence.jpa.outbox.order.mapping.OrderShippedModelToDomainMapper;
import io.github.hirannor.oms.adapter.persistence.jpa.outbox.payment.PaymentCanceledModel;
import io.github.hirannor.oms.adapter.persistence.jpa.outbox.payment.PaymentFailedModel;
import io.github.hirannor.oms.adapter.persistence.jpa.outbox.payment.PaymentSucceededModel;
import io.github.hirannor.oms.adapter.persistence.jpa.outbox.payment.mapping.PaymentCanceledModelToDomainMapper;
import io.github.hirannor.oms.adapter.persistence.jpa.outbox.payment.mapping.PaymentFailedModelToDomainMapper;
import io.github.hirannor.oms.adapter.persistence.jpa.outbox.payment.mapping.PaymentSucceededModelToDomainMapper;
import io.github.hirannor.oms.domain.order.events.OrderCreated;
import io.github.hirannor.oms.domain.order.events.OrderPaid;
import io.github.hirannor.oms.domain.order.events.OrderProcessing;
import io.github.hirannor.oms.domain.order.events.OrderShipped;
import io.github.hirannor.oms.domain.payment.events.PaymentCanceled;
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
    private final Function<OrderProcessingModel, OrderProcessing> mapOrderProcessingModelToDomain;
    private final Function<OrderShippedModel, OrderShipped> mapOrderShippedModelToDomain;

    public DomainEventModelToEventMapper() {
        this.mapOrderCreatedModelToDomain = new OrderCreatedModelToDomainMapper();
        this.mapPaymentSucceededModelToDomain = new PaymentSucceededModelToDomainMapper();
        this.mapPaymentFailedModelToDomain = new PaymentFailedModelToDomainMapper();
        this.mapPaymentCanceledModelToDomain = new PaymentCanceledModelToDomainMapper();
        this.mapOrderPaidModelToDomain = new OrderPaidModelToDomainMapper();
        this.mapOrderProcessingModelToDomain = new OrderProcessingModelToDomainMapper();
        this.mapOrderShippedModelToDomain = new OrderShippedModelToDomainMapper();
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
            default -> null;
        };
    }
}
