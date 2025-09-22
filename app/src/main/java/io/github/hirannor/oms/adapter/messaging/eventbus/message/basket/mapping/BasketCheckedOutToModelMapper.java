package io.github.hirannor.oms.adapter.messaging.eventbus.message.basket.mapping;

import io.github.hirannor.oms.adapter.messaging.eventbus.message.basket.BasketCheckedOutModel;
import io.github.hirannor.oms.adapter.messaging.eventbus.message.basket.BasketItemModel;
import io.github.hirannor.oms.domain.basket.BasketItem;
import io.github.hirannor.oms.domain.basket.events.BasketCheckedOut;

import java.util.List;
import java.util.function.Function;

public class BasketCheckedOutToModelMapper implements Function<BasketCheckedOut, BasketCheckedOutModel> {
    private final Function<BasketItem, BasketItemModel> mapBasketItemToModel;

    public BasketCheckedOutToModelMapper() {
        this.mapBasketItemToModel = new BasketItemToModelMapper();
    }

    @Override
    public BasketCheckedOutModel apply(final BasketCheckedOut evt) {
       if (evt == null) return null;

       final List<BasketItemModel> items = evt.items()
                .stream()
                .map(mapBasketItemToModel)
                .toList();

        return new BasketCheckedOutModel(
               evt.id().asText(),
               evt.customerId().asText(),
                items,
                evt.occurredAt()
       );
    }
}
