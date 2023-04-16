package com.hirannor.hexagonal.infrastructure.command;


import java.util.UUID;

public record CommandId(UUID id) {
    public CommandId {
        if (id == null) throw new IllegalArgumentException("CommandId cannot be null!");
    }

    public static CommandId from(final String value) {
        return new CommandId(UUID.fromString(value));
    }

    public static CommandId generate() {
        return new CommandId(UUID.randomUUID());
    }

    public String asText() {
        return this.id.toString();
    }
}
