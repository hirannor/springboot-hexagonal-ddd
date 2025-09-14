package hu.hirannor.hexagonal.application.service.basket;

import hu.hirannor.hexagonal.application.usecase.basket.BasketDisplaying;
import hu.hirannor.hexagonal.domain.CustomerId;
import hu.hirannor.hexagonal.domain.basket.Basket;
import hu.hirannor.hexagonal.domain.basket.BasketId;
import hu.hirannor.hexagonal.domain.basket.BasketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BasketQueryService implements BasketDisplaying {
    private final BasketRepository baskets;

    @Autowired
    BasketQueryService(final BasketRepository baskets) {
        this.baskets = baskets;
    }

    @Override
    public Optional<Basket> displayBy(final CustomerId customer) {
        return baskets.findBy(customer);
    }

    @Override
    public Optional<Basket> displayBy(final BasketId basket) {
        return baskets.findBy(basket);
    }

    @Override
    public List<Basket> displayAll() {
        return baskets.findAll();
    }
}
