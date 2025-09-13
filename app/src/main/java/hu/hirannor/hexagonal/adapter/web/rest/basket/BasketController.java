package hu.hirannor.hexagonal.adapter.web.rest.basket;

import hu.hirannor.hexagonal.adapter.web.rest.baskets.api.BasketsApi;
import hu.hirannor.hexagonal.adapter.web.rest.baskets.model.BasketItemModel;
import hu.hirannor.hexagonal.adapter.web.rest.baskets.model.BasketModel;
import hu.hirannor.hexagonal.adapter.web.rest.baskets.model.CreateBasketModel;
import hu.hirannor.hexagonal.application.usecase.basket.*;
import hu.hirannor.hexagonal.infrastructure.adapter.DriverAdapter;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@DriverAdapter
class BasketController implements BasketsApi {

    private final BasketCheckout basketCheckout;
    private final BasketCreation basketCreation;
    private final BasketDeletion basketDeletion;
    private final BasketDisplaying baskets;
    private final BasketProductAddition basketProductAddition;
    private final BasketProductRemoval basketProductRemoval;

    BasketController(
            final BasketCheckout basketCheckout,
            final BasketCreation basketCreation,
            final BasketDeletion basketDeletion,
            final BasketDisplaying baskets,
            final BasketProductAddition basketProductAddition,
            final BasketProductRemoval basketProductRemoval) {
        this.basketCheckout = basketCheckout;
        this.basketCreation = basketCreation;
        this.basketDeletion = basketDeletion;
        this.baskets = baskets;
        this.basketProductAddition = basketProductAddition;
        this.basketProductRemoval = basketProductRemoval;
    }

    @Override
    @PreAuthorize("hasRole('Customer')")
    public ResponseEntity<BasketModel> addItem(final UUID basketId, final BasketItemModel basketItemModel) {
        return BasketsApi.super.addItem(basketId, basketItemModel);
    }

    @Override
    @PreAuthorize("hasRole('Customer')")
    public ResponseEntity<BasketModel> checkout(final UUID basketId) {
        return BasketsApi.super.checkout(basketId);
    }

    @Override
    @PreAuthorize("hasRole('Customer')")
    public ResponseEntity<BasketModel> createBasket(final CreateBasketModel createBasketModel) {
        return BasketsApi.super.createBasket(createBasketModel);
    }

    @Override
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<List<BasketModel>> displayAll() {
        return BasketsApi.super.displayAll();
    }

    @Override
    @PreAuthorize("hasRole('Customer')")
    public ResponseEntity<BasketModel> displayBy(final UUID basketId) {
        return BasketsApi.super.displayBy(basketId);
    }

    @Override
    @PreAuthorize("hasRole('Customer')")
    public ResponseEntity<BasketModel> removeItem(final UUID basketId, final BasketItemModel basketItemModel) {
        return BasketsApi.super.removeItem(basketId, basketItemModel);
    }
}
