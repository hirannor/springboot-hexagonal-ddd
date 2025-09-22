package io.github.hirannor.oms.infrastructure.messaging;

import java.util.UUID;

public record MessageId(UUID uniqueComponent) {
    public MessageId {
        if (uniqueComponent == null)
            throw new IllegalArgumentException("Message can't be null");
    }

    public static MessageId from(final String source) {
        if (source == null || source.isBlank()) {
            throw new IllegalArgumentException(
                    "Message can't be null or empty"
            );
        }

        return new MessageId(UUID.fromString(source));
    }

    public String asText() {
        return this.uniqueComponent.toString();
    }
}
