package io.github.hirannor.oms.infrastructure.messaging;

import java.util.UUID;

/**
 * Base interface for messages.
 *
 * @author Mate Karolyi
 */
public interface Message {
    MessageId id();

    static MessageId generateId() {
        return new MessageId(UUID.randomUUID());
    }
}
