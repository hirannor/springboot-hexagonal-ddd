package hu.hirannor.hexagonal.application.usecase.notification;

import hu.hirannor.hexagonal.application.port.notification.SystemNotificationType;
import hu.hirannor.hexagonal.domain.order.OrderId;
import hu.hirannor.hexagonal.infrastructure.command.Command;
import hu.hirannor.hexagonal.infrastructure.command.CommandId;

public record SendSystemNotification(CommandId id, OrderId order, SystemNotificationType notificationType) implements
    Command {

    public static SendSystemNotification issue(final OrderId order, final SystemNotificationType evt) {
        return new SendSystemNotification(CommandId.generate(), order, evt);
    }
}
