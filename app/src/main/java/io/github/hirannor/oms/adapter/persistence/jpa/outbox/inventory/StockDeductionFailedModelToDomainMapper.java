package io.github.hirannor.oms.adapter.persistence.jpa.outbox.inventory;

import io.github.hirannor.oms.domain.inventory.InventoryId;
import io.github.hirannor.oms.domain.inventory.events.StockDeductionFailed;
import io.github.hirannor.oms.domain.product.ProductId;
import io.github.hirannor.oms.infrastructure.messaging.MessageId;

import java.util.function.Function;

public class StockDeductionFailedModelToDomainMapper implements Function<StockDeductionFailedModel, StockDeductionFailed> {
    public StockDeductionFailedModelToDomainMapper() {
    }

    @Override
    public StockDeductionFailed apply(final StockDeductionFailedModel model) {
        if (model == null) return null;

        return StockDeductionFailed.recreate(
                MessageId.from(model.eventId()),
                InventoryId.from(model.inventoryId()),
                ProductId.from(model.productId()),
                model.requestedQuantity(),
                model.reservedQuantity(),
                model.availableQuantity()
        );
    }
}
