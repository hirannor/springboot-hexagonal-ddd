package io.github.hirannor.oms.application.usecase.inventory;

import io.github.hirannor.oms.domain.inventory.command.ReleaseStock;

public interface StockReleasing {
    void release(ReleaseStock cmd);
}
