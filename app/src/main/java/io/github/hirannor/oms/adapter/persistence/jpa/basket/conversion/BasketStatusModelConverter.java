package io.github.hirannor.oms.adapter.persistence.jpa.basket.conversion;

import io.github.hirannor.oms.adapter.persistence.jpa.basket.BasketStatusModel;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class BasketStatusModelConverter implements AttributeConverter<BasketStatusModel, String> {

    @Override
    public String convertToDatabaseColumn(final BasketStatusModel status) {
        if (status == null) return null;
        return status.dbRepresentation();
    }

    @Override
    public BasketStatusModel convertToEntityAttribute(final String dbData) {
        if (dbData == null) return null;
        return BasketStatusModel.from(dbData);
    }
}
