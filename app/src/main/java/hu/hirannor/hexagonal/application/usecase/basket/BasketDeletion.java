package hu.hirannor.hexagonal.application.usecase.basket;

import hu.hirannor.hexagonal.domain.CustomerId;

public interface BasketDeletion {
    void deleteBy(CustomerId customer);
}
