package hu.hirannor.hexagonal.application.port.notification;

import hu.hirannor.hexagonal.domain.EmailAddress;
import hu.hirannor.hexagonal.domain.customer.Address;
import hu.hirannor.hexagonal.domain.customer.FullName;
import hu.hirannor.hexagonal.domain.order.OrderId;

public record NotificationData(
        SystemNotificationType type,
        OrderId orderId,
        String customerName,
        EmailAddress email,
        Address address
) {}
