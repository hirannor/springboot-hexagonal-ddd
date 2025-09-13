package hu.hirannor.hexagonal.adapter.persistence.jpa.order.mapping;

import hu.hirannor.hexagonal.adapter.persistence.jpa.CurrencyModel;
import hu.hirannor.hexagonal.adapter.persistence.jpa.CurrencyModelToDomainMapper;
import hu.hirannor.hexagonal.adapter.persistence.jpa.order.OrderedProductModel;
import hu.hirannor.hexagonal.domain.Currency;
import hu.hirannor.hexagonal.domain.Money;
import hu.hirannor.hexagonal.domain.order.OrderedProduct;
import hu.hirannor.hexagonal.domain.product.ProductId;

import java.util.function.Function;

public class OrderedProductModelToDomainMapper implements Function<OrderedProductModel, OrderedProduct> {

    private final Function<CurrencyModel, Currency> mapCurrencyModelToDomain;

    public OrderedProductModelToDomainMapper() {
        this.mapCurrencyModelToDomain = new CurrencyModelToDomainMapper();
    }

    @Override
    public OrderedProduct apply(final OrderedProductModel model) {
        if (model == null) return null;

        final Currency currency = mapCurrencyModelToDomain.apply(model.getPriceCurrency());

        return OrderedProduct.create(
                ProductId.from(model.getProductId()),
                model.getQuantity(),
                Money.of(
                        model.getPriceAmount(),
                        currency
                ));
    }
}
