package io.github.hirannor.oms.adapter.persistence.jpa.product.mapping;

import io.github.hirannor.oms.adapter.persistence.jpa.CurrencyModel;
import io.github.hirannor.oms.adapter.persistence.jpa.CurrencyModelToDomainMapper;
import io.github.hirannor.oms.adapter.persistence.jpa.product.ProductModel;
import io.github.hirannor.oms.domain.core.valueobject.Currency;
import io.github.hirannor.oms.domain.core.valueobject.Money;
import io.github.hirannor.oms.domain.product.Product;
import io.github.hirannor.oms.domain.product.ProductId;

import java.util.function.Function;

public class ProductModelToDomainMapper implements Function<ProductModel, Product> {

    private final Function<CurrencyModel, Currency> mapModelToCurrency;

    public ProductModelToDomainMapper() {
        this.mapModelToCurrency = new CurrencyModelToDomainMapper();
    }

    @Override
    public Product apply(final ProductModel model) {
        if (model == null) return null;

        final Currency currency = mapModelToCurrency.apply(model.getPriceCurrency());

        return Product.empty()
                .id(ProductId.from(model.getProductId()))
                .name(model.getName())
                .description(model.getDescription())
                .price(
                        Money.of(
                                model.getPriceAmount(),
                                currency
                        )
                )
                .assemble();
    }
}
