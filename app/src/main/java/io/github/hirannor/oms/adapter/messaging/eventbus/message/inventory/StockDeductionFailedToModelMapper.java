package io.github.hirannor.oms.adapter.messaging.eventbus.message.inventory;

import io.github.hirannor.oms.domain.inventory.events.StockDeductionFailed;

import java.util.function.Function;

public class StockDeductionFailedToModelMapper implements Function<StockDeductionFailed, StockDeductionFailedModel> {
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
}
