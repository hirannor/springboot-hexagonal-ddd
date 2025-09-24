package io.github.hirannor.oms.application.usecase.inventory;

import io.github.hirannor.oms.domain.inventory.command.DeductStock;

public interface StockDeduction {
    void deduct(DeductStock cmd);
}
