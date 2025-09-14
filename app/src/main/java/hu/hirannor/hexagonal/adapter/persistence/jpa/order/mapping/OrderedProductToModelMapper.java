package hu.hirannor.hexagonal.adapter.persistence.jpa.order.mapping;

import hu.hirannor.hexagonal.adapter.persistence.jpa.CurrencyModel;
import hu.hirannor.hexagonal.adapter.persistence.jpa.CurrencyToModelMapper;
import hu.hirannor.hexagonal.adapter.persistence.jpa.order.OrderedProductModel;
import hu.hirannor.hexagonal.domain.Currency;
import hu.hirannor.hexagonal.domain.order.OrderedProduct;

import java.util.function.Function;

public class OrderedProductToModelMapper implements Function<OrderedProduct, OrderedProductModel> {

    private final Function<Currency, CurrencyModel> mapCurrencyToModel;

    public OrderedProductToModelMapper() {
        this.mapCurrencyToModel = new CurrencyToModelMapper();
    }

    @Override
    public OrderedProductModel apply(final OrderedProduct domain) {
        if (domain == null) return null;

        final OrderedProductModel model = new OrderedProductModel();
        model.setProductId(domain.productId().asText());
        model.setQuantity(domain.quantity());
        model.setPriceAmount(domain.price().amount());
        model.setPriceCurrency(mapCurrencyToModel.apply(domain.price().currency()));

        return model;
    }
}
