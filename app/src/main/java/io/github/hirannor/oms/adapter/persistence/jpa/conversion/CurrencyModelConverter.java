package io.github.hirannor.oms.adapter.persistence.jpa.conversion;

import io.github.hirannor.oms.adapter.persistence.jpa.CurrencyModel;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class CurrencyModelConverter
        implements AttributeConverter<CurrencyModel, String> {

    CurrencyModelConverter() {
    }

    @Override
    public String convertToDatabaseColumn(final CurrencyModel currency) {
        if (currency == null) return null;

        return currency.dbRepresentation();
    }

    @Override
    public CurrencyModel convertToEntityAttribute(final String dbData) {
        if (dbData == null) return null;

        return CurrencyModel.from(dbData);
    }
}