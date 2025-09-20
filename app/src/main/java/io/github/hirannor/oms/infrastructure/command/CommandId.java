package io.github.hirannor.oms.infrastructure.command;


import java.util.UUID;

/**
 * Immutable record to hold command value
 *
 * @param value {@link UUID} unique identifier of command
 * @author Mate Karolyi
 */
public record CommandId(UUID value) {

    /**
     * Default constructor
     *
     * @param value {@link UUID} unique identifier of command
     */
    public CommandId {
        if (value == null) throw new IllegalArgumentException("CommandId cannot be null!");
    }

    /**
     * Create an instance of {@link CommandId}
     *
     * @param value {@link String} raw string value of a UUID notificationType
     * @return an instance of {@link CommandId} notificationType
     */
    public static CommandId from(final String value) {
        return new CommandId(UUID.fromString(value));
    }

    /**
     * Generates an unique {@link CommandId} command value.
     *
     * @return an instance of {@link CommandId} notificationType
     */
    public static CommandId generate() {
        return new CommandId(UUID.randomUUID());
    }

}
