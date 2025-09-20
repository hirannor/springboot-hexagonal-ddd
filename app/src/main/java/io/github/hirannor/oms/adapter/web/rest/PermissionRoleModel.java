package io.github.hirannor.oms.adapter.web.rest;

import java.util.Objects;

public enum PermissionRoleModel {
    ADMIN("Admin"),
    CUSTOMER("Customer");

    private final String value;

    PermissionRoleModel(final String value) {
        this.value = value;
    }

    public static PermissionRoleModel from(final String text) {
        Objects.requireNonNull(text, "Text must not be null");

        for (PermissionRoleModel role : PermissionRoleModel.values()) {
            if (role.value.equalsIgnoreCase(text)) {
                return role;
            }
        }

        throw new IllegalArgumentException(String.format("Unexpected value '%s'", text));
    }

    public String value() {
        return this.value;
    }
}
