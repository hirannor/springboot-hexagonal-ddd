package hu.hirannor.hexagonal.adapter.persistence.jpa.order.mapping;

import hu.hirannor.hexagonal.adapter.persistence.jpa.CurrencyModel;
import hu.hirannor.hexagonal.adapter.persistence.jpa.CurrencyModelToDomainMapper;
import hu.hirannor.hexagonal.adapter.persistence.jpa.order.OrderItemsModel;
import hu.hirannor.hexagonal.domain.Currency;
import hu.hirannor.hexagonal.domain.Money;
import hu.hirannor.hexagonal.domain.order.OrderItem;
import hu.hirannor.hexagonal.domain.product.ProductId;

import java.util.function.Function;

public class OrderedItemModelToDomainMapper implements Function<OrderItemsModel, OrderItem> {

    private final Function<CurrencyModel, Currency> mapCurrencyModelToDomain;

    public OrderedItemModelToDomainMapper() {
        this.mapCurrencyModelToDomain = new CurrencyModelToDomainMapper();
    }

    @Override
    public OrderItem apply(final OrderItemsModel model) {
        if (model == null) return null;

        final Currency currency = mapCurrencyModelToDomain.apply(model.getPriceCurrency());

        return OrderItem.create(
                ProductId.from(model.getProductId()),
                model.getQuantity(),
                Money.of(
                        model.getPriceAmount(),
                        currency
                ));
    }
}
