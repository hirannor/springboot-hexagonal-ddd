package io.github.hirannor.oms.adapter.persistence.jpa.outbox;


import java.util.Objects;

public enum OutboxStatusModel {

    PENDING("PENDING"),
    PUBLISHED("PUBLISHED"),
    FAILED("FAILED");

    private final String dbRepresentation;

    OutboxStatusModel(final String dbRepresentation) {
        this.dbRepresentation = dbRepresentation;
    }

    public static OutboxStatusModel from(final String text) {
        Objects.requireNonNull(text, "Status text cannot be null");
        for (final OutboxStatusModel status : OutboxStatusModel.values()) {
            if (status.dbRepresentation.equalsIgnoreCase(text)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unexpected OutboxStatus value: " + text);
    }

    public String dbRepresentation() {
        return this.dbRepresentation;
    }
}
