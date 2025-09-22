package io.github.hirannor.oms.application.service.basket;

import io.github.hirannor.oms.application.usecase.basket.BasketDisplaying;
import io.github.hirannor.oms.domain.basket.Basket;
import io.github.hirannor.oms.domain.basket.BasketId;
import io.github.hirannor.oms.domain.basket.BasketItem;
import io.github.hirannor.oms.domain.basket.BasketRepository;
import io.github.hirannor.oms.domain.core.valueobject.CustomerId;
import io.github.hirannor.oms.domain.product.Product;
import io.github.hirannor.oms.domain.product.ProductId;
import io.github.hirannor.oms.domain.product.ProductRepository;
import io.github.hirannor.oms.infrastructure.application.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

@ApplicationService
public class BasketQueryService implements BasketDisplaying {

    private final BasketRepository baskets;
    private final ProductRepository products;
    private final BiFunction<Basket, Map<ProductId, Product>, BasketView> mapBasketToView;

    @Autowired
    BasketQueryService(final BasketRepository baskets, final ProductRepository products) {
        this.baskets = baskets;
        this.products = products;
        this.mapBasketToView = new BasketToViewMapper();
    }

    @Override
    public Optional<BasketView> displayBy(final CustomerId customer) {
        if (customer == null) throw new IllegalArgumentException("CustomerId is null");

        return baskets.findBy(customer)
                .map(this::mapToView);
    }

    @Override
    public Optional<BasketView> displayBy(final BasketId basketId) {
        if (basketId == null) throw new IllegalArgumentException("basket is null");

        return baskets.findBy(basketId)
                .map(this::mapToView);
    }

    @Override
    public List<BasketView> displayAll() {
        return baskets.findAll().stream()
                .map(this::mapToView)
                .toList();
    }

    private BasketView mapToView(final Basket basket) {
        final List<ProductId> productIds = basket.items().stream()
                .map(BasketItem::productId)
                .toList();

        final Map<ProductId, Product> productMap = products.findAllBy(productIds)
                .stream()
                .collect(Collectors.toMap(Product::id, p -> p));

        return mapBasketToView.apply(basket, productMap);
    }
}
