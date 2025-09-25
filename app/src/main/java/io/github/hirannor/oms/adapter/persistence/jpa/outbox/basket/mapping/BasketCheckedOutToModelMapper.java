package io.github.hirannor.oms.adapter.persistence.jpa.outbox.basket.mapping;

import io.github.hirannor.oms.adapter.persistence.jpa.outbox.DomainEventMapper;
import io.github.hirannor.oms.adapter.persistence.jpa.outbox.basket.BasketCheckedOutModel;
import io.github.hirannor.oms.adapter.persistence.jpa.outbox.basket.BasketItemModel;
import io.github.hirannor.oms.domain.basket.BasketItem;
import io.github.hirannor.oms.domain.basket.events.BasketCheckedOut;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;

@Component(value = "BasketCheckedOutToModelMapper")
public class BasketCheckedOutToModelMapper implements DomainEventMapper<BasketCheckedOut, BasketCheckedOutModel> {
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

    @Override
    public Class<BasketCheckedOut> eventType() {
        return BasketCheckedOut.class;
    }
}
