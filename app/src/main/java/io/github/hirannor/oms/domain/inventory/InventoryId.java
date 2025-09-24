package io.github.hirannor.oms.domain.inventory;

import java.util.UUID;

public record InventoryId(String value) {

    public InventoryId {
        if (value == null || value.isBlank())
            throw new IllegalArgumentException("InventoryId cannot be null or empty!");
    }

    public static InventoryId from(final String value) {
        return new InventoryId(value);
    }

    public static InventoryId generate() {
        return new InventoryId(UUID.randomUUID().toString());
    }

    public String asText() {
        return this.value;
    }
}
