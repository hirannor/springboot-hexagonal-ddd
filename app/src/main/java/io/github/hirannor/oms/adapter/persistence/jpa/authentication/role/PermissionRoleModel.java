package io.github.hirannor.oms.adapter.persistence.jpa.authentication.role;

import java.util.Objects;

public enum PermissionRoleModel {
    ADMIN("Admin"),
    CUSTOMER("Customer");

    private final String dbRepresentation;

    PermissionRoleModel(final String dbRepresentation) {
        this.dbRepresentation = dbRepresentation;
    }

    public static PermissionRoleModel from(final String text) {
        Objects.requireNonNull(text, "Text must not be null");

        for (PermissionRoleModel role : PermissionRoleModel.values()) {
            if (role.dbRepresentation.equalsIgnoreCase(text)) {
                return role;
            }
        }

        throw new IllegalArgumentException(String.format("Unexpected value '%s'", text));
    }

    public String dbRepresentation() {
        return this.dbRepresentation;
    }
}
