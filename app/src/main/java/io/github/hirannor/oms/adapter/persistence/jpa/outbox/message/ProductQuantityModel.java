package io.github.hirannor.oms.adapter.persistence.jpa.outbox.message;

public record ProductQuantityModel(String productId, int quantity) {
    public static ProductQuantityModel of(final String productId, int quantity) {
        return new ProductQuantityModel(productId, quantity);
    }
}
