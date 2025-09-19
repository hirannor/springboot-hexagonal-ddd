package hu.hirannor.hexagonal.adapter.web.rest.basket;

import hu.hirannor.hexagonal.adapter.web.rest.basket.mapping.*;
import hu.hirannor.hexagonal.adapter.web.rest.baskets.api.BasketsApi;
import hu.hirannor.hexagonal.adapter.web.rest.baskets.model.*;
import hu.hirannor.hexagonal.application.service.basket.BasketView;
import hu.hirannor.hexagonal.application.usecase.basket.*;
import hu.hirannor.hexagonal.domain.basket.*;
import hu.hirannor.hexagonal.domain.basket.command.*;
import hu.hirannor.hexagonal.domain.core.valueobject.CustomerId;
import hu.hirannor.hexagonal.infrastructure.adapter.DriverAdapter;
import java.net.URI;
import java.util.List;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api")
@DriverAdapter
class BasketController implements BasketsApi {

    private final Function<BasketView, BasketModel> mapBasketViewToModel;
    private final Function<Basket, BasketModel> mapBasketToModel;
    private final Function<CreateBasketModel, CreateBasket> mapCreateBasketModelToCommand;
    private final Function<BasketItemModel, BasketItem> mapBasketItemModelToDomain;

    private final BasketCheckout basket;
    private final BasketCreation basketCreator;
    private final BasketDisplaying baskets;
    private final BasketProductHandling basketHandler;

    @Autowired
    BasketController(
            final BasketCheckout basket,
            final BasketCreation basketCreator,
            final BasketDisplaying baskets,
            final BasketProductHandling basketHandler) {
      this(
          basket,
          basketCreator,
          baskets,
          basketHandler,
          new BasketViewToModelMapper(),
          new CreateBasketModelToCommandMapper(),
          new BasketItemModelToDomainMapper(),
          new BasketToModelMapper()
      );
    }

    BasketController(
            final BasketCheckout basket,
            final BasketCreation basketCreator,
            final BasketDisplaying baskets,
            final BasketProductHandling basketHandler,
            final Function<BasketView, BasketModel> mapBasketViewToModel,
            final Function<CreateBasketModel, CreateBasket> mapCreateBasketModelToCommand,
            final Function<BasketItemModel, BasketItem> mapBasketItemToModel,
            final Function<Basket, BasketModel> mapBasketToModel) {
        this.basket = basket;
        this.basketCreator = basketCreator;
        this.baskets = baskets;
        this.basketHandler = basketHandler;
        this.mapBasketViewToModel = mapBasketViewToModel;
        this.mapCreateBasketModelToCommand = mapCreateBasketModelToCommand;
        this.mapBasketItemModelToDomain = mapBasketItemToModel;
        this.mapBasketToModel = mapBasketToModel;
    }

    @Override
    public ResponseEntity<BasketModel> checkout(final String customerId) {
        final BasketView view = this.basket.checkout(
            CheckoutBasket.issue(CustomerId.from(customerId))
        );

        return ResponseEntity.ok().body(mapBasketViewToModel.apply(view));
    }

    @Override
    public ResponseEntity<BasketModel> createBasket(final CreateBasketModel createBasketModel) {
        final CreateBasket command = mapCreateBasketModelToCommand.apply(createBasketModel);

        final Basket basket = basketCreator.create(command);

        final URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{customerId}")
                .buildAndExpand(command.customerId().asText())
                .toUri();

        return ResponseEntity.created(location)
            .body(mapBasketToModel.apply(basket));
    }

    @Override
    public ResponseEntity<List<BasketModel>> displayAll() {
        final List<BasketModel> baskets = this.baskets.displayAll()
                .stream()
                .map(mapBasketViewToModel)
                .toList();

        return ResponseEntity.ok(baskets);
    }

    @Override
    public ResponseEntity<BasketModel> displayBy(final String customerId) {
        return baskets.displayBy(CustomerId.from(customerId))
                .map(mapBasketViewToModel)
                .map(ResponseEntity::ok)
                .orElseGet(ResponseEntity.notFound()::build);
    }

    @Override
    public ResponseEntity<Void> addItem(final String basketId, final BasketItemModel basketItemModel) {
        final BasketItem item = mapBasketItemModelToDomain.apply(basketItemModel);
        final AddBasketItem command = AddBasketItem.issue(
            BasketId.from(basketId),
            item
        );
        basketHandler.add(command);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> removeItem(final String basketId, final BasketItemModel basketItemModel) {
        final BasketItem item = mapBasketItemModelToDomain.apply(basketItemModel);
        final RemoveBasketItem command = RemoveBasketItem.issue(
            BasketId.from(basketId),
            item
        );
        basketHandler.remove(command);
        return ResponseEntity.noContent().build();
    }
}
