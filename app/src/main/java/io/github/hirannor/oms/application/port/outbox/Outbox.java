package io.github.hirannor.oms.application.port.outbox;

import io.github.hirannor.oms.infrastructure.event.DomainEvent;
import io.github.hirannor.oms.infrastructure.messaging.MessageId;

import java.time.Instant;
import java.util.List;

public interface Outbox {
    void save(DomainEvent evt);

    List<DomainEvent> findUnprocessed();

    void markAsProcessed(MessageId id);

    void deleteProcessedOlderThan(Instant time);
}
