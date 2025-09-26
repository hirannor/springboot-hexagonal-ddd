package io.github.hirannor.oms.adapter.persistence.jpa.outbox.message.inventory;

import io.github.hirannor.oms.adapter.persistence.jpa.outbox.message.MessageModelMapper;
import io.github.hirannor.oms.domain.inventory.InventoryId;
import io.github.hirannor.oms.domain.inventory.events.StockDeductionFailed;
import io.github.hirannor.oms.domain.product.ProductId;
import org.springframework.stereotype.Component;

@Component(value = "StockDeductionFailedModelToDomainMapper")
public class StockDeductionFailedModelToDomainMapper implements MessageModelMapper<StockDeductionFailedModel, StockDeductionFailed> {
    public StockDeductionFailedModelToDomainMapper() {
    }

    @Override
    public StockDeductionFailed apply(final StockDeductionFailedModel model) {
        if (model == null) return null;

        return StockDeductionFailed.recreate(
                model.id(),
                InventoryId.from(model.inventoryId()),
                ProductId.from(model.productId()),
                model.requestedQuantity(),
                model.reservedQuantity(),
                model.availableQuantity()
        );
    }

    @Override
    public Class<StockDeductionFailedModel> messageType() {
        return StockDeductionFailedModel.class;
    }
}
