package hu.hirannor.hexagonal.adapter.persistence.jpa.product.mapping;

import hu.hirannor.hexagonal.adapter.persistence.jpa.CurrencyModel;
import hu.hirannor.hexagonal.adapter.persistence.jpa.CurrencyToModelMapper;
import hu.hirannor.hexagonal.adapter.persistence.jpa.product.ProductModel;
import hu.hirannor.hexagonal.domain.core.valueobject.Currency;
import hu.hirannor.hexagonal.domain.product.Product;

import java.util.function.Function;

public class ProductToModelMapper implements Function<Product, ProductModel> {

    private final Function<Currency, CurrencyModel> mapDomainToModel;

    public ProductToModelMapper() {
        this.mapDomainToModel = new CurrencyToModelMapper();
    }

    @Override
    public ProductModel apply(final Product domain) {
        final ProductModel model = new ProductModel();

        model.setProductId(domain.id().asText());
        model.setName(domain.name());
        model.setDescription(domain.description());
        model.setPriceAmount(domain.price().amount());
        model.setPriceCurrency(mapDomainToModel.apply(domain.price().currency()));

        return model;
    }
}
