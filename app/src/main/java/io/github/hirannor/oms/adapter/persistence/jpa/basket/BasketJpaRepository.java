package io.github.hirannor.oms.adapter.persistence.jpa.basket;

import io.github.hirannor.oms.adapter.persistence.jpa.basket.mapping.BasketModelToDomainMapper;
import io.github.hirannor.oms.adapter.persistence.jpa.basket.mapping.BasketModeller;
import io.github.hirannor.oms.domain.basket.Basket;
import io.github.hirannor.oms.domain.basket.BasketId;
import io.github.hirannor.oms.domain.basket.BasketRepository;
import io.github.hirannor.oms.domain.core.valueobject.CustomerId;
import io.github.hirannor.oms.infrastructure.adapter.DrivenAdapter;
import io.github.hirannor.oms.infrastructure.adapter.PersistenceAdapter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@DrivenAdapter
@PersistenceAdapter
class BasketJpaRepository implements BasketRepository {
    private final Function<BasketModel, Basket> mapModelToDomain;

    private final BasketSpringDataJpaRepository baskets;

    @Autowired
    BasketJpaRepository(final BasketSpringDataJpaRepository baskets) {
        this(baskets, new BasketModelToDomainMapper());
    }

    BasketJpaRepository(final BasketSpringDataJpaRepository baskets,
                        final Function<BasketModel, Basket> mapModelToDomain) {
        this.baskets = baskets;
        this.mapModelToDomain = mapModelToDomain;
    }

    @Override
    public Optional<Basket> findBy(final CustomerId customerId) {
        if (customerId == null) throw new IllegalArgumentException("customerId cannot be null");

        return baskets.findByCustomerId(customerId.asText())
                .map(mapModelToDomain);
    }

    @Override
    public Optional<Basket> findBy(final BasketId basketId) {
        if (basketId == null) throw new IllegalArgumentException("basketId cannot be null");

        return baskets.findByBasketId(basketId.asText())
                .map(mapModelToDomain);
    }

    @Override
    public List<Basket> findAll() {
        return baskets.findAll()
                .stream()
                .map(mapModelToDomain)
                .toList();
    }

    @Override
    public void save(final Basket basket) {
        if (basket == null) throw new IllegalArgumentException("basket cannot be null");

        final BasketModel toPersist = baskets.findByBasketId(basket.id().asText())
                .orElseGet(BasketModel::new);

        BasketModeller.applyChangesFrom(basket).to(toPersist);
        baskets.save(toPersist);
    }

    @Override
    public void deleteBy(final CustomerId customerId) {
        if (customerId == null) throw new IllegalArgumentException("customerId cannot be null");

        baskets.deleteByCustomerId(customerId.asText());
    }
}
