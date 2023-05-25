package com.hirannor.hexagonal.adapter.persistence.jpa.customer.conversion;


import com.hirannor.hexagonal.adapter.persistence.jpa.customer.model.CountryModel;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * A {@link AttributeConverter} converter implementation to convert
 * {@link CountryModel} type to {@link String} and vica-versa
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
