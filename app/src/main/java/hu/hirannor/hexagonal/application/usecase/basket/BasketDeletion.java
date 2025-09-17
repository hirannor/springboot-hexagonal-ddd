package hu.hirannor.hexagonal.application.usecase.basket;

import hu.hirannor.hexagonal.domain.core.valueobject.CustomerId;

public interface BasketDeletion {
    void deleteBy(CustomerId customer);
}
