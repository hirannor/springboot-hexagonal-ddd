package hu.hirannor.hexagonal.adapter.persistence.jpa.product.mapping;

import hu.hirannor.hexagonal.adapter.persistence.jpa.CurrencyModel;
import hu.hirannor.hexagonal.adapter.persistence.jpa.CurrencyModelToDomainMapper;
import hu.hirannor.hexagonal.adapter.persistence.jpa.product.ProductModel;
import hu.hirannor.hexagonal.domain.Currency;
import hu.hirannor.hexagonal.domain.Money;
import hu.hirannor.hexagonal.domain.product.Product;
import hu.hirannor.hexagonal.domain.product.ProductId;

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
