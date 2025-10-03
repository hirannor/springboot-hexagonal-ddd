package io.github.hirannor.oms.application.port.outbox;

import io.github.hirannor.oms.infrastructure.messaging.Message;
import io.github.hirannor.oms.infrastructure.messaging.MessageId;

import java.time.Instant;
import java.util.List;

public interface Outbox {
    void save(Message msg);

    List<Message> findAllPendingBy(int batchSize);

    void markAsPublished(MessageId id);

    void deletePublishedOlderThan(Instant time);

    void markAsFailed(MessageId id, Throwable reason);

}
