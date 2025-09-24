package io.github.hirannor.oms.domain.product;

public record ProductName(String value) {

    public ProductName {
        if (value == null || value.isBlank())
            throw new IllegalArgumentException("ProductName cannot be null or empty!");
    }

    public static ProductName from(final String value) {
        return new ProductName(value);
    }

    public String asText() {
        return this.value;
    }
}
