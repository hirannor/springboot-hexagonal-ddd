package hu.hirannor.hexagonal.adapter.web.rest.basket;

import hu.hirannor.hexagonal.adapter.web.rest.baskets.api.BasketsApi;
import hu.hirannor.hexagonal.adapter.web.rest.baskets.model.BasketItemModel;
import hu.hirannor.hexagonal.adapter.web.rest.baskets.model.BasketModel;
import hu.hirannor.hexagonal.adapter.web.rest.baskets.model.CreateBasketModel;
import hu.hirannor.hexagonal.application.usecase.basket.*;
import hu.hirannor.hexagonal.domain.CustomerId;
import hu.hirannor.hexagonal.domain.basket.Basket;
import hu.hirannor.hexagonal.domain.basket.BasketId;
import hu.hirannor.hexagonal.domain.basket.BasketItem;
import hu.hirannor.hexagonal.domain.basket.command.AddBasketItem;
import hu.hirannor.hexagonal.domain.basket.command.CheckoutBasket;
import hu.hirannor.hexagonal.domain.basket.command.CreateBasket;
import hu.hirannor.hexagonal.domain.basket.command.RemoveBasketItem;
import hu.hirannor.hexagonal.infrastructure.adapter.DriverAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.function.Function;

@RestController
@RequestMapping("/api")
@DriverAdapter
class BasketController implements BasketsApi {

    private final Function<Basket, BasketModel> mapBasketToModel;
    private final Function<CreateBasketModel, CreateBasket> mapCreateBasketModelToCommand;
    private final Function<BasketItemModel, BasketItem> mapBasketItemToModel;

    private final BasketCheckout basketCheckout;
    private final BasketCreation basketCreation;
    private final BasketDisplaying baskets;
    private final BasketProductHandling basketHandling;

    @Autowired
    BasketController(
            final BasketCheckout basketCheckout,
            final BasketCreation basketCreation,
            final BasketDisplaying baskets,
            final BasketProductHandling basketHandling) {
      this(
          basketCheckout,
          basketCreation,
          baskets,
          basketHandling,
          new BasketToModelMapper(),
          new CreateBasketModelToCommandMapper(),
          new BasketItemModelToDomainMapper()
      );
    }

    BasketController(
            final BasketCheckout basketCheckout,
            final BasketCreation basketCreation,
            final BasketDisplaying baskets,
            final BasketProductHandling basketHandling,
            final Function<Basket, BasketModel> mapBasketToModel,
            final Function<CreateBasketModel, CreateBasket> mapCreateBasketModelToCommand,
            final Function<BasketItemModel, BasketItem> mapBasketItemToModel) {
        this.basketCheckout = basketCheckout;
        this.basketCreation = basketCreation;
        this.baskets = baskets;
        this.basketHandling = basketHandling;
        this.mapBasketToModel = mapBasketToModel;
        this.mapCreateBasketModelToCommand = mapCreateBasketModelToCommand;
        this.mapBasketItemToModel = mapBasketItemToModel;
    }

    @Override
    @PreAuthorize("hasRole('Customer')")
    public ResponseEntity<BasketModel> checkout(final String customerId) {
        final Basket basket = basketCheckout.checkout(
            CheckoutBasket.issue(CustomerId.from(customerId))
        );

        return ResponseEntity.ok().body(mapBasketToModel.apply(basket));
    }

    @Override
    @PreAuthorize("hasRole('Customer')")
    public ResponseEntity<BasketModel> createBasket(final CreateBasketModel createBasketModel) {
        final CreateBasket command = mapCreateBasketModelToCommand.apply(createBasketModel);

        final Basket basket = basketCreation.create(command);

        final URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(basket.id().value())
                .toUri();

        return ResponseEntity.created(location)
                .body(mapBasketToModel.apply(basket));
    }

    @Override
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<List<BasketModel>> displayAll() {
        final List<BasketModel> baskets = this.baskets.displayAll()
                .stream()
                .map(mapBasketToModel)
                .toList();

        return ResponseEntity.ok(baskets);
    }

    @Override
    @PreAuthorize("hasRole('Customer')")
    public ResponseEntity<BasketModel> displayBy(final String customerId) {
        return baskets.displayBy(CustomerId.from(customerId))
                .map(mapBasketToModel)
                .map(ResponseEntity::ok)
                .orElseGet(ResponseEntity.notFound()::build);
    }

    @Override
    @PreAuthorize("hasRole('Customer')")
    public ResponseEntity<BasketModel> displayByBasketId(final String basketId) {
        return baskets.displayBy(BasketId.from(basketId))
                .map(mapBasketToModel)
                .map(ResponseEntity::ok)
                .orElseGet(ResponseEntity.notFound()::build);
    }

    @Override
    @PreAuthorize("hasRole('Customer')")
    public ResponseEntity<Void> addItem(final String basketId, final BasketItemModel basketItemModel) {
        final BasketItem item = mapBasketItemToModel.apply(basketItemModel);
        final AddBasketItem command = AddBasketItem.issue(
            BasketId.from(basketId),
            item
        );
        basketHandling.add(command);
        return ResponseEntity.ok().build();
    }

    @Override
    @PreAuthorize("hasRole('Customer')")
    public ResponseEntity<Void> removeItem(final String basketId, final BasketItemModel basketItemModel) {
        final BasketItem item = mapBasketItemToModel.apply(basketItemModel);
        final RemoveBasketItem command = RemoveBasketItem.issue(
            BasketId.from(basketId),
            item
        );
        basketHandling.remove(command);
        return ResponseEntity.ok().build();
    }
}
