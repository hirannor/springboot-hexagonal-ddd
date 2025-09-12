package hu.hirannor.hexagonal.adapter.persistence.jpa.authentication.role;


import hu.hirannor.hexagonal.domain.authentication.Role;

import java.util.function.Function;

class PermissionRoleModelToRole implements Function<PermissionRoleModel, Role> {

    PermissionRoleModelToRole() {}

    @Override
    public Role apply(final PermissionRoleModel model) {
        if (model == null) throw new IllegalArgumentException("model cannot be null");

        return switch (model) {
            case CUSTOMER -> Role.CUSTOMER;
            case ADMIN -> Role.ADMIN;
        };
    }
}
