package io.github.hirannor.oms.application.port.notification;

import io.github.hirannor.oms.domain.core.valueobject.EmailAddress;
import io.github.hirannor.oms.domain.customer.Address;
import io.github.hirannor.oms.domain.order.OrderId;

public record NotificationData(
        SystemNotificationType type,
        OrderId orderId,
        String customerName,
        EmailAddress email,
        Address address
) {}
