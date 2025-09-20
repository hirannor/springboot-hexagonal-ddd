package io.github.hirannor.oms.adapter.persistence.jpa.payment.conversion;

import io.github.hirannor.oms.adapter.persistence.jpa.payment.PaymentStatusModel;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class PaymentStatusModelConverter implements AttributeConverter<PaymentStatusModel, String> {

    @Override
    public String convertToDatabaseColumn(final PaymentStatusModel status) {
        if (status == null) return null;
        return status.dbRepresentation();
    }

    @Override
    public PaymentStatusModel convertToEntityAttribute(final String dbData) {
        if (dbData == null) return null;
        return PaymentStatusModel.from(dbData);
    }
}
