package io.github.hirannor.oms.adapter.persistence.jpa.outbox.message.basket;

import io.github.hirannor.oms.adapter.persistence.jpa.outbox.message.MessageModel;
import io.github.hirannor.oms.infrastructure.messaging.MessageId;

import java.time.Instant;
import java.util.List;


public record BasketCheckedOutModel(
        MessageId id,
        String customerId,
        List<BasketItemModel> items,
        Instant occurredAt
) implements MessageModel {
}
