package io.github.hirannor.oms.adapter.authentication.jwt;

import java.util.Objects;

public enum RoleModel {
    ADMIN("Admin"),
    CUSTOMER("Customer");

    private final String displayText;

    RoleModel(final String displayText) {
        this.displayText = displayText;
    }

    public static RoleModel from(final String text) {
        Objects.requireNonNull(text, "Text must not be null");

        for (RoleModel role : RoleModel.values()) {
            if (role.displayText.equalsIgnoreCase(text)) {
                return role;
            }
        }

        throw new IllegalArgumentException(String.format("Unexpected value '%s'", text));
    }

    public String displayText() {
        return this.displayText;
    }
}
