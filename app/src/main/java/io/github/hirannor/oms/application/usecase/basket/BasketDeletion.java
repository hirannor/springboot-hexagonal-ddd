package io.github.hirannor.oms.application.usecase.basket;

import io.github.hirannor.oms.domain.core.valueobject.CustomerId;

public interface BasketDeletion {
    void deleteBy(CustomerId customer);
}
