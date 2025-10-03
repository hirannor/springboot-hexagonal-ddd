package io.github.hirannor.oms.application.service.outbox;

import io.github.hirannor.oms.application.port.outbox.Outbox;
import io.github.hirannor.oms.infrastructure.application.ApplicationService;
import io.github.hirannor.oms.infrastructure.messaging.MessageId;
import org.springframework.beans.factory.annotation.Autowired;

@ApplicationService
class OutboxService implements OutboxProcessing {
    private final Outbox outboxes;

    @Autowired
    OutboxService(final Outbox outboxes) {
        this.outboxes = outboxes;
    }

    @Override
    public void markAsPublished(final MessageId message) {
        if (message == null) throw new IllegalArgumentException("Message cannot be null");

        outboxes.markAsPublished(message);
    }

    @Override
    public void markAsFailed(final MessageId message, final Throwable throwable) {
        outboxes.markAsFailed(message, throwable);
    }
}
