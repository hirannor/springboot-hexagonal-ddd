package io.github.hirannor.oms.adapter.persistence.jpa.outbox.message.inventory;

import io.github.hirannor.oms.adapter.persistence.jpa.outbox.message.MessageMapper;
import io.github.hirannor.oms.domain.inventory.events.StockDeductionFailed;
import org.springframework.stereotype.Component;

@Component(value = "StockDeductionFailedToModelMapper")
public class StockDeductionFailedToModelMapper implements MessageMapper<StockDeductionFailed, StockDeductionFailedModel> {
    public StockDeductionFailedToModelMapper() {
    }

    @Override
    public StockDeductionFailedModel apply(final StockDeductionFailed evt) {
        if (evt == null) return null;

        return new StockDeductionFailedModel(
                evt.id(),
                evt.inventoryId().asText(),
                evt.productId().asText(),
                evt.requestedQuantity(),
                evt.reservedQuantity(),
                evt.availableQuantity()
        );
    }

    @Override
    public Class<StockDeductionFailed> messageType() {
        return StockDeductionFailed.class;
    }
}
