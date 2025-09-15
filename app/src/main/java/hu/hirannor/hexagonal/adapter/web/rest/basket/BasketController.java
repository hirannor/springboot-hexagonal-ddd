package hu.hirannor.hexagonal.adapter.web.rest.basket;

import hu.hirannor.hexagonal.adapter.web.rest.basket.mapping.BasketItemModelToDomainMapper;
import hu.hirannor.hexagonal.adapter.web.rest.basket.mapping.BasketToModelMapper;
import hu.hirannor.hexagonal.adapter.web.rest.basket.mapping.CreateBasketModelToCommandMapper;
import hu.hirannor.hexagonal.adapter.web.rest.baskets.api.BasketsApi;
import hu.hirannor.hexagonal.adapter.web.rest.baskets.model.BasketItemModel;
import hu.hirannor.hexagonal.adapter.web.rest.baskets.model.BasketModel;
import hu.hirannor.hexagonal.adapter.web.rest.baskets.model.CreateBasketModel;
import hu.hirannor.hexagonal.application.usecase.basket.BasketCheckout;
import hu.hirannor.hexagonal.application.usecase.basket.BasketCreation;
import hu.hirannor.hexagonal.application.usecase.basket.BasketDisplaying;
import hu.hirannor.hexagonal.application.usecase.basket.BasketProductHandling;
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
          new BasketToModelMapper(),
          new CreateBasketModelToCommandMapper(),
          new BasketItemModelToDomainMapper()
      );
    }

    BasketController(
            final BasketCheckout basket,
            final BasketCreation basketCreator,
            final BasketDisplaying baskets,
            final BasketProductHandling basketHandler,
            final Function<Basket, BasketModel> mapBasketToModel,
            final Function<CreateBasketModel, CreateBasket> mapCreateBasketModelToCommand,
            final Function<BasketItemModel, BasketItem> mapBasketItemToModel) {
        this.basket = basket;
        this.basketCreator = basketCreator;
        this.baskets = baskets;
        this.basketHandler = basketHandler;
        this.mapBasketToModel = mapBasketToModel;
        this.mapCreateBasketModelToCommand = mapCreateBasketModelToCommand;
        this.mapBasketItemToModel = mapBasketItemToModel;
    }

    @Override
    public ResponseEntity<BasketModel> checkout(final String customerId) {
        final Basket basket = this.basket.checkout(
            CheckoutBasket.issue(CustomerId.from(customerId))
        );

        return ResponseEntity.ok().body(mapBasketToModel.apply(basket));
    }

    @Override
    public ResponseEntity<BasketModel> createBasket(final CreateBasketModel createBasketModel) {
        final CreateBasket command = mapCreateBasketModelToCommand.apply(createBasketModel);

        final Basket basket = basketCreator.create(command);

        final URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(basket.id().value())
                .toUri();

        return ResponseEntity.created(location)
                .body(mapBasketToModel.apply(basket));
    }

    @Override
    public ResponseEntity<List<BasketModel>> displayAll() {
        final List<BasketModel> baskets = this.baskets.displayAll()
                .stream()
                .map(mapBasketToModel)
                .toList();

        return ResponseEntity.ok(baskets);
    }

    @Override
    public ResponseEntity<BasketModel> displayBy(final String customerId) {
        return baskets.displayBy(CustomerId.from(customerId))
                .map(mapBasketToModel)
                .map(ResponseEntity::ok)
                .orElseGet(ResponseEntity.notFound()::build);
    }

    @Override
    public ResponseEntity<Void> addItem(final String basketId, final BasketItemModel basketItemModel) {
        final BasketItem item = mapBasketItemToModel.apply(basketItemModel);
        final AddBasketItem command = AddBasketItem.issue(
            BasketId.from(basketId),
            item
        );
        basketHandler.add(command);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> removeItem(final String basketId, final BasketItemModel basketItemModel) {
        final BasketItem item = mapBasketItemToModel.apply(basketItemModel);
        final RemoveBasketItem command = RemoveBasketItem.issue(
            BasketId.from(basketId),
            item
        );
        basketHandler.remove(command);
        return ResponseEntity.ok().build();
    }
}
