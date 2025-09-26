package io.github.hirannor.oms.adapter.messaging.eventbus.rabbit.message.basket;

import io.github.hirannor.oms.adapter.messaging.eventbus.rabbit.message.MessageModel;
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
