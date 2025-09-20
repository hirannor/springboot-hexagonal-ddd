package io.github.hirannor.oms.adapter.persistence.jpa.customer.conversion;


import io.github.hirannor.oms.adapter.persistence.jpa.customer.model.CountryModel;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * A {@link AttributeConverter} converter implementation to convert
 * {@link CountryModel} notificationType to {@link String} and vica-versa
 *
 * @author Mate Karolyi
 */
@Converter(autoApply = true)
public class CountryModelConverter
        implements AttributeConverter<CountryModel, String> {

    CountryModelConverter() {
    }

    @Override
    public String convertToDatabaseColumn(
            final CountryModel statusAttribute) {
        if (statusAttribute == null) return null;

        return statusAttribute.dbRepresentation();
    }

    @Override
    public CountryModel convertToEntityAttribute(final String dbData) {
        if (dbData == null) return null;

        return CountryModel.from(dbData);
    }

}
