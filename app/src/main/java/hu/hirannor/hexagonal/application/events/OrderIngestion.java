package hu.hirannor.hexagonal.application.events;

import hu.hirannor.hexagonal.application.usecase.basket.BasketDeletion;
import hu.hirannor.hexagonal.application.usecase.order.ChangeOrderStatus;
import hu.hirannor.hexagonal.application.usecase.order.OrderCreation;
import hu.hirannor.hexagonal.application.usecase.order.OrderStatusChanging;
import hu.hirannor.hexagonal.domain.basket.BasketItem;
import hu.hirannor.hexagonal.domain.basket.events.BasketCheckedOut;
import hu.hirannor.hexagonal.domain.order.OrderStatus;
import hu.hirannor.hexagonal.domain.order.OrderedProduct;
import hu.hirannor.hexagonal.domain.order.command.CreateOrder;
import hu.hirannor.hexagonal.domain.order.events.OrderCreated;
import hu.hirannor.hexagonal.domain.order.events.OrderPaid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class OrderIngestion {
    private static final Logger LOGGER = LogManager.getLogger(
        OrderIngestion.class
    );

    private final Function<BasketItem, OrderedProduct> mapBasketItemToOrderedProduct;

    private final OrderCreation orderCreation;
    private final OrderStatusChanging orderStatusChanging;
    private final BasketDeletion basketDeletion;

    @Autowired
    OrderIngestion(
            final OrderCreation orderCreation,
            final OrderStatusChanging orderStatusChanging,
            final BasketDeletion basketDeletion) {
     this(
         orderCreation,
         orderStatusChanging,
         basketDeletion,
         new BasketItemToOrderedItemMapper()
     );
    }

    OrderIngestion(
            final OrderCreation orderCreation,
            final OrderStatusChanging orderStatusChanging,
            final BasketDeletion basketDeletion,
            final Function<BasketItem, OrderedProduct> mapBasketItemToOrderedProduct) {
        this.orderCreation = orderCreation;
        this.orderStatusChanging = orderStatusChanging;
        this.basketDeletion = basketDeletion;
        this.mapBasketItemToOrderedProduct = mapBasketItemToOrderedProduct;
    }


    @TransactionalEventListener
    public void handle(final BasketCheckedOut evt) {
        if (evt == null) throw new IllegalArgumentException("BasketCheckedOut event cannot be null!");

        LOGGER.debug("BasketCheckedOut event received: {}", evt);

        final Set<OrderedProduct> orderedItems = evt.items()
                .stream()
                .map(mapBasketItemToOrderedProduct)
                .collect(Collectors.toSet());

        orderCreation.create(
            CreateOrder.issue(
                evt.customerId(),
                orderedItems
            )
        );
    }

    @TransactionalEventListener
    public void handle(final OrderCreated evt) {
        if (evt == null) throw new IllegalArgumentException("OrderCreated event cannot be null!");

        LOGGER.debug("OrderCreated event received: {}", evt);

        orderStatusChanging.change(
            ChangeOrderStatus.issue(
                evt.orderId(),
                OrderStatus.WAITING_FOR_PAYMENT
            )
        );

        basketDeletion.deleteBy(evt.customerId());
    }

    @TransactionalEventListener
    public void handle(final OrderPaid evt) {
        if (evt == null) throw new IllegalArgumentException("OrderPaid event cannot be null!");

        LOGGER.debug("OrderPaid event received: {}", evt);

        orderStatusChanging.change(
                ChangeOrderStatus.issue(
                        evt.orderId(),
                        OrderStatus.PROCESSING
                )
        );
    }

}
