package com.hirannor.hexagonal.adapter.persistence.jpa.conversion;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.Timestamp;
import java.time.Instant;

@Converter(autoApply = true)
class InstantConverter implements AttributeConverter<Instant, Timestamp> {

    InstantConverter() {}

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
