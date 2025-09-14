package hu.hirannor.hexagonal.application.service.basket;

import hu.hirannor.hexagonal.application.usecase.basket.BasketCheckout;
import hu.hirannor.hexagonal.application.usecase.basket.BasketCreation;
import hu.hirannor.hexagonal.application.usecase.basket.BasketDeletion;
import hu.hirannor.hexagonal.application.usecase.basket.BasketProductHandling;
import hu.hirannor.hexagonal.domain.CustomerId;
import hu.hirannor.hexagonal.domain.basket.Basket;
import hu.hirannor.hexagonal.domain.basket.BasketId;
import hu.hirannor.hexagonal.domain.basket.BasketRepository;
import hu.hirannor.hexagonal.domain.basket.command.AddBasketItem;
import hu.hirannor.hexagonal.domain.basket.command.CheckoutBasket;
import hu.hirannor.hexagonal.domain.basket.command.CreateBasket;
import hu.hirannor.hexagonal.domain.basket.command.RemoveBasketItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Supplier;

@Service
@Transactional(
    propagation = Propagation.REQUIRES_NEW,
    isolation = Isolation.REPEATABLE_READ
)
class BasketCommandService implements
        BasketCreation,
        BasketDeletion,
        BasketCheckout,
        BasketProductHandling {

    private final BasketRepository baskets;

    @Autowired
    BasketCommandService(final BasketRepository baskets) {
        this.baskets = baskets;
    }

    @Override
    public Basket create(final CreateBasket creation) {
        if (creation == null) throw new IllegalArgumentException("CustomerId is null");

        final Basket basket = Basket.create(creation);

        baskets.save(basket);

        return basket;
    }

    @Override
    public void deleteBy(final CustomerId customer) {
        if (customer == null) throw new IllegalArgumentException("CustomerId is null");

        baskets.deleteBy(customer);
    }

    @Override
    public Basket checkout(final CheckoutBasket command) {
        if (command == null) throw new IllegalArgumentException("command is null");

        final Basket basket = baskets.findBy(command.customerId())
                .orElseThrow(failBecauseBasketWasNotFoundBy(command.customerId()));

        basket.checkout();
        baskets.save(basket);

        return basket;
    }

    @Override
    public void add(final AddBasketItem command) {
        final Basket basket = baskets.findBy(command.basketId())
                .orElseThrow(failBecauseBasketWasNotFoundBy(command.basketId()));

        basket.addProduct(command.item());
        baskets.save(basket);
    }

    @Override
    public void remove(final RemoveBasketItem command) {
        final Basket basket = baskets.findBy(command.basketId())
                .orElseThrow(failBecauseBasketWasNotFoundBy(command.basketId()));

        basket.removeProduct(command.item());
        baskets.save(basket);
    }

    private static Supplier<IllegalStateException> failBecauseBasketWasNotFoundBy(final CustomerId customerId) {
        return () -> new IllegalStateException("Basket with id " + customerId + " not found");
    }

    private static Supplier<IllegalStateException> failBecauseBasketWasNotFoundBy(final BasketId basketId) {
        return () -> new IllegalStateException("Basket with id " + basketId + " not found");
    }
}
