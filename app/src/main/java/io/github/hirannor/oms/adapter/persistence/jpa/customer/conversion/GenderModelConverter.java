package io.github.hirannor.oms.adapter.persistence.jpa.customer.conversion;


import io.github.hirannor.oms.adapter.persistence.jpa.customer.model.GenderModel;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * A {@link AttributeConverter} converter implementation to convert
 * {@link GenderModel} notificationType to {@link String} and vica-versa
 *
 * @author Mate Karolyi
 */
@Converter(autoApply = true)
public class GenderModelConverter
        implements AttributeConverter<GenderModel, String> {

    GenderModelConverter() {
    }

    @Override
    public String convertToDatabaseColumn(
            final GenderModel statusAttribute) {
        if (statusAttribute == null) return null;

        return statusAttribute.dbRepresentation();
    }

    @Override
    public GenderModel convertToEntityAttribute(final String dbData) {
        if (dbData == null) return null;

        return GenderModel.from(dbData);
    }
}
