package io.github.hirannor.oms.application.usecase.notification;

import io.github.hirannor.oms.application.port.notification.SystemNotificationType;
import io.github.hirannor.oms.domain.order.OrderId;
import io.github.hirannor.oms.infrastructure.command.Command;
import io.github.hirannor.oms.infrastructure.command.CommandId;

public record SendSystemNotification(CommandId id, OrderId order, SystemNotificationType notificationType) implements
        Command {

    public static SendSystemNotification issue(final OrderId order, final SystemNotificationType evt) {
        return new SendSystemNotification(CommandId.generate(), order, evt);
    }
}
