package io.github.hirannor.oms.adapter.messaging.eventbus.rabbit.message;

public record ProductQuantityModel(String productId, int quantity) {
    public static ProductQuantityModel of(final String productId, int quantity) {
        return new ProductQuantityModel(productId, quantity);
    }
}
