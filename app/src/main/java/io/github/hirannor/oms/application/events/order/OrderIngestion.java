package io.github.hirannor.oms.application.events.order;

import io.github.hirannor.oms.application.usecase.inventory.StockReleasing;
import io.github.hirannor.oms.application.usecase.order.OrderCancellation;
import io.github.hirannor.oms.application.usecase.order.OrderPaymentProcessing;
import io.github.hirannor.oms.application.usecase.order.StartOrderPaymentProcessing;
import io.github.hirannor.oms.domain.inventory.command.ReleaseStock;
import io.github.hirannor.oms.domain.order.events.OrderPaid;
import io.github.hirannor.oms.domain.order.events.OrderPaymentExpired;
import io.github.hirannor.oms.domain.order.events.OrderPaymentFailed;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class OrderIngestion {
    private static final Logger LOGGER = LogManager.getLogger(
            OrderIngestion.class
    );

    private final OrderPaymentProcessing order;
    private final StockReleasing stock;
    private final OrderCancellation orderCancellation;

    @Autowired
    OrderIngestion(final OrderPaymentProcessing order,
                   final StockReleasing stock,
                   final OrderCancellation orderCancellation) {
        this.order = order;
        this.stock = stock;
        this.orderCancellation = orderCancellation;
    }

    @EventListener
    public void handle(final OrderPaid evt) {
        if (evt == null) throw new IllegalArgumentException("OrderPaid event cannot be null!");

        LOGGER.debug("OrderPaid event received: {}", evt);

        order.startProcessing(StartOrderPaymentProcessing.issue(
                evt.orderId(),
                evt.products()
        ));
    }

    @EventListener
    public void handle(final OrderPaymentFailed evt) {
        if (evt == null) throw new IllegalArgumentException("OrderPaymentFailed event cannot be null!");

        LOGGER.debug("OrderPaymentFailed event received: {}", evt);
    }

    @EventListener
    public void handle(final OrderPaymentExpired evt) {
        if (evt == null) throw new IllegalArgumentException("OrderPaymentExpired event cannot be null!");

        LOGGER.debug("OrderPaymentExpired event received: {}", evt);

        orderCancellation.cancelBy(evt.orderId());

        stock.release(ReleaseStock.issue(
                evt.orderId(),
                evt.products()
        ));
    }

}
