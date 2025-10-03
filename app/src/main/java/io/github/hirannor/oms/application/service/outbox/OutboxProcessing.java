package io.github.hirannor.oms.application.service.outbox;

import io.github.hirannor.oms.infrastructure.messaging.MessageId;

public interface OutboxProcessing {
    void markAsPublished(MessageId message);

    void markAsFailed(MessageId message, Throwable throwable);
}
