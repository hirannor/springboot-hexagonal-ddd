package io.github.hirannor.oms.adapter.messaging.eventbus.rabbit.message.inventory;

import io.github.hirannor.oms.adapter.messaging.eventbus.rabbit.message.DomainEventMapper;
import io.github.hirannor.oms.domain.inventory.events.StockDeductionFailed;
import org.springframework.stereotype.Component;

@Component
public class StockDeductionFailedToModelMapper implements DomainEventMapper<StockDeductionFailed, StockDeductionFailedModel> {
    public StockDeductionFailedToModelMapper() {
    }

    @Override
    public StockDeductionFailedModel apply(final StockDeductionFailed evt) {
        if (evt == null) return null;

        return new StockDeductionFailedModel(
                evt.id().asText(),
                evt.inventoryId().asText(),
                evt.productId().asText(),
                evt.requestedQuantity(),
                evt.reservedQuantity(),
                evt.availableQuantity()
        );
    }

    @Override
    public Class<StockDeductionFailed> eventType() {
        return StockDeductionFailed.class;
    }
}
