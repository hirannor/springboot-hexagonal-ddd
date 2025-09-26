package io.github.hirannor.oms.adapter.messaging.eventbus.rabbit.message.payment;

import io.github.hirannor.oms.adapter.messaging.eventbus.rabbit.message.MessageModel;
import io.github.hirannor.oms.infrastructure.messaging.MessageId;

public record PaymentExpiredModel(
        MessageId id,
        String paymentId,
        String orderId
) implements MessageModel {
}