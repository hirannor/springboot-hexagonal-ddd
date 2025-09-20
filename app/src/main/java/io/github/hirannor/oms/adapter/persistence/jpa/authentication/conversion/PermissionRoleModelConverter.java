package io.github.hirannor.oms.adapter.persistence.jpa.authentication.conversion;


import io.github.hirannor.oms.adapter.persistence.jpa.authentication.role.PermissionRoleModel;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * JPA {@link AttributeConverter} to convert {@link PermissionRoleModel} to {@link String} and vice versa.
 */
@Converter(autoApply = true)
public class PermissionRoleModelConverter implements AttributeConverter<PermissionRoleModel, String> {

    public PermissionRoleModelConverter() {
    }

    @Override
    public String convertToDatabaseColumn(final PermissionRoleModel attribute) {
        if (attribute == null) return null;
        return attribute.dbRepresentation();
    }

    @Override
    public PermissionRoleModel convertToEntityAttribute(final String dbData) {
        if (dbData == null) return null;
        return PermissionRoleModel.from(dbData);
    }
}
