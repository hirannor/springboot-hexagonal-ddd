package io.github.hirannor.oms.adapter.persistence.jpa.order.conversion;

import io.github.hirannor.oms.adapter.persistence.jpa.order.OrderStatusModel;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class OrderStatusModelConverter implements AttributeConverter<OrderStatusModel, String> {

    @Override
    public String convertToDatabaseColumn(final OrderStatusModel status) {
        if (status == null) return null;
        return status.dbRepresentation();
    }

    @Override
    public OrderStatusModel convertToEntityAttribute(final String dbData) {
        if (dbData == null) return null;
        return OrderStatusModel.from(dbData);
    }
}
