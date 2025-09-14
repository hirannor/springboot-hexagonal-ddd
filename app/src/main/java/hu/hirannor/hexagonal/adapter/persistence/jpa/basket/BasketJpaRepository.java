package hu.hirannor.hexagonal.adapter.persistence.jpa.basket;

import hu.hirannor.hexagonal.adapter.persistence.jpa.basket.mapping.BasketModelToDomainMapper;
import hu.hirannor.hexagonal.adapter.persistence.jpa.basket.mapping.BasketModeller;
import hu.hirannor.hexagonal.adapter.persistence.jpa.basket.mapping.BasketToModelMapper;
import hu.hirannor.hexagonal.domain.CustomerId;
import hu.hirannor.hexagonal.domain.basket.Basket;
import hu.hirannor.hexagonal.domain.basket.BasketId;
import hu.hirannor.hexagonal.domain.basket.BasketRepository;
import hu.hirannor.hexagonal.infrastructure.adapter.DrivenAdapter;
import hu.hirannor.hexagonal.infrastructure.event.EventPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Repository
@Transactional(
    propagation = Propagation.MANDATORY,
    isolation = Isolation.REPEATABLE_READ
)
@DrivenAdapter
class BasketJpaRepository implements BasketRepository {
    private final Function<BasketModel, Basket> mapModelToDomain;
    private final Function<Basket, BasketModel> mapDomainToModel;

    private final BasketSpringDataJpaRepository baskets;

    @Autowired
    BasketJpaRepository(final BasketSpringDataJpaRepository baskets) {
        this(baskets, new BasketModelToDomainMapper(), new BasketToModelMapper());
    }

    BasketJpaRepository(final BasketSpringDataJpaRepository baskets,
                        final Function<BasketModel, Basket> mapModelToDomain,
                        final Function<Basket, BasketModel> mapDomainToModel) {
        this.baskets = baskets;
        this.mapModelToDomain = mapModelToDomain;
        this.mapDomainToModel = mapDomainToModel;
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
    @EventPublisher
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
