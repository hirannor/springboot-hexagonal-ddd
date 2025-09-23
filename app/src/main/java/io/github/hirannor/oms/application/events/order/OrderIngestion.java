package io.github.hirannor.oms.application.events.order;

import io.github.hirannor.oms.application.usecase.order.ChangeOrderStatus;
import io.github.hirannor.oms.application.usecase.order.OrderStatusChanging;
import io.github.hirannor.oms.domain.order.OrderStatus;
import io.github.hirannor.oms.domain.order.events.OrderPaid;
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

    private final OrderStatusChanging status;

    @Autowired
    OrderIngestion(final OrderStatusChanging status) {
        this.status = status;
    }


    @EventListener
    public void handle(final OrderPaid evt) {
        if (evt == null) throw new IllegalArgumentException("OrderPaid event cannot be null!");

        LOGGER.debug("OrderPaid event received: {}", evt);

        status.change(ChangeOrderStatus.issue(evt.orderId(), OrderStatus.PROCESSING));
    }

}
