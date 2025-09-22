package io.github.hirannor.oms.adapter.web.rest.basket;

import io.github.hirannor.oms.adapter.web.rest.basket.mapping.BasketItemModelToDomainMapper;
import io.github.hirannor.oms.adapter.web.rest.basket.mapping.BasketToModelMapper;
import io.github.hirannor.oms.adapter.web.rest.basket.mapping.BasketViewToModelMapper;
import io.github.hirannor.oms.adapter.web.rest.basket.mapping.CreateBasketModelToCommandMapper;
import io.github.hirannor.oms.adapter.web.rest.baskets.api.BasketsApi;
import io.github.hirannor.oms.adapter.web.rest.baskets.model.BasketItemModel;
import io.github.hirannor.oms.adapter.web.rest.baskets.model.BasketModel;
import io.github.hirannor.oms.adapter.web.rest.baskets.model.CreateBasketModel;
import io.github.hirannor.oms.application.service.basket.BasketView;
import io.github.hirannor.oms.application.usecase.basket.BasketCheckout;
import io.github.hirannor.oms.application.usecase.basket.BasketCreation;
import io.github.hirannor.oms.application.usecase.basket.BasketDisplaying;
import io.github.hirannor.oms.application.usecase.basket.BasketProductHandling;
import io.github.hirannor.oms.domain.basket.Basket;
import io.github.hirannor.oms.domain.basket.BasketId;
import io.github.hirannor.oms.domain.basket.BasketItem;
import io.github.hirannor.oms.domain.basket.command.AddBasketItem;
import io.github.hirannor.oms.domain.basket.command.CheckoutBasket;
import io.github.hirannor.oms.domain.basket.command.CreateBasket;
import io.github.hirannor.oms.domain.basket.command.RemoveBasketItem;
import io.github.hirannor.oms.domain.core.valueobject.CustomerId;
import io.github.hirannor.oms.infrastructure.adapter.DriverAdapter;
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
