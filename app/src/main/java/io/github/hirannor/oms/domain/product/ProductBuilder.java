package io.github.hirannor.oms.domain.product;

import io.github.hirannor.oms.domain.core.valueobject.Money;

public class ProductBuilder {
    private ProductId id;
    private String name;
    private String description;
    private Money price;

    public ProductBuilder id(final ProductId id) {
        this.id = id;
        return this;
    }

    public ProductBuilder name(final String name) {
        this.name = name;
        return this;
    }

    public ProductBuilder description(final String description) {
        this.description = description;
        return this;
    }

    public ProductBuilder price(Money price) {
        this.price = price;
        return this;
    }

    public Product assemble() {
        return new Product(id, name, description, price);
    }
}