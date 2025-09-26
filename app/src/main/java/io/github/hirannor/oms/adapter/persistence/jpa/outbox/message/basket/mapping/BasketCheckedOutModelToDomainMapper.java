package io.github.hirannor.oms.adapter.persistence.jpa.outbox.message.basket.mapping;

import io.github.hirannor.oms.adapter.persistence.jpa.outbox.message.MessageModelMapper;
import io.github.hirannor.oms.adapter.persistence.jpa.outbox.message.basket.BasketCheckedOutModel;
import io.github.hirannor.oms.adapter.persistence.jpa.outbox.message.basket.BasketItemModel;
import io.github.hirannor.oms.domain.basket.BasketItem;
import io.github.hirannor.oms.domain.basket.events.BasketCheckedOut;
import io.github.hirannor.oms.domain.core.valueobject.CustomerId;
import io.github.hirannor.oms.infrastructure.messaging.MessageId;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;

@Component(value = "BasketCheckedOutModelToDomainMapper")
public class BasketCheckedOutModelToDomainMapper implements MessageModelMapper<BasketCheckedOutModel, BasketCheckedOut> {

    private final Function<BasketItemModel, BasketItem> mapBasketItemModelToDomain;

    public BasketCheckedOutModelToDomainMapper() {
        this.mapBasketItemModelToDomain = new BasketItemModelToDomainMapper();
    }

    @Override
    public BasketCheckedOut apply(final BasketCheckedOutModel model) {
        if (model == null) return null;

        final List<BasketItem> items = model.getItems()
                .stream()
                .map(mapBasketItemModelToDomain)
                .toList();

        return BasketCheckedOut.recreate(
                MessageId.from(model.getEventId()),
                CustomerId.from(model.getCustomerId()),
                items
        );

    }

    @Override
    public Class<BasketCheckedOutModel> eventType() {
        return BasketCheckedOutModel.class;
    }
}
