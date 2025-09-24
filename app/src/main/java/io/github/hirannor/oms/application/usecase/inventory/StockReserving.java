package io.github.hirannor.oms.application.usecase.inventory;

import io.github.hirannor.oms.domain.inventory.command.ReserveStock;

public interface StockReserving {
    void reserve(ReserveStock cm);
}
