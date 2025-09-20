package io.github.hirannor.oms.adapter.persistence.jpa.conversion;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.sql.Timestamp;
import java.time.Instant;

/**
 * A {@link AttributeConverter} converter implementation to convert
 * {@link Instant} notificationType to {@link Timestamp} and vica-versa
 *
 * @author Mate Karolyi
 */
@Converter(autoApply = true)
class InstantConverter implements AttributeConverter<Instant, Timestamp> {

    InstantConverter() {
    }

    @Override
    public Timestamp convertToDatabaseColumn(final Instant attribute) {
        if (attribute == null) return null;

        return Timestamp.from(attribute);
    }

    @Override
    public Instant convertToEntityAttribute(final Timestamp dbData) {
        if (dbData == null) return null;

        return dbData.toInstant();
    }

}
