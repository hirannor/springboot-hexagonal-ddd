package com.hirannor.hexagonal.adapter.persistence.jpa.conversion;

import java.sql.Timestamp;
import java.time.Instant;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * A {@link AttributeConverter} converter implementation to convert
 * {@link Instant} type to {@link Timestamp} and vica-versa
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
