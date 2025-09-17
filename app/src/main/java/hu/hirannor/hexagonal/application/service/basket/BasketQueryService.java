package hu.hirannor.hexagonal.application.service.basket;

import hu.hirannor.hexagonal.application.usecase.basket.BasketDisplaying;
import hu.hirannor.hexagonal.domain.basket.Basket;
import hu.hirannor.hexagonal.domain.basket.BasketId;
import hu.hirannor.hexagonal.domain.basket.BasketRepository;
import hu.hirannor.hexagonal.domain.core.valueobject.CustomerId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(
    propagation = Propagation.REQUIRES_NEW,
    isolation = Isolation.REPEATABLE_READ
)
public class BasketQueryService implements BasketDisplaying {
    private final BasketRepository baskets;

    @Autowired
    BasketQueryService(final BasketRepository baskets) {
        this.baskets = baskets;
    }

    @Override
    public Optional<Basket> displayBy(final CustomerId customer) {
        if (customer == null) throw new IllegalArgumentException("CustomerId is null");

        return baskets.findBy(customer);
    }

    @Override
    public Optional<Basket> displayBy(final BasketId basket) {
        if (basket == null) throw new IllegalArgumentException("basket is null");
        return baskets.findBy(basket);
    }

    @Override
    public List<Basket> displayAll() {
        return baskets.findAll();
    }
}
