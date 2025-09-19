package hu.hirannor.hexagonal.adapter.authentication.jwt;

import hu.hirannor.hexagonal.domain.authentication.Role;
import java.util.function.Function;

class RoleModelToRoleMapper implements Function<RoleModel, Role> {

    RoleModelToRoleMapper() {}

    @Override
    public Role apply(final RoleModel model) {
        if (model == null) throw new IllegalArgumentException("model cannot be null");

        return switch (model) {
            case CUSTOMER -> Role.CUSTOMER;
            case ADMIN -> Role.ADMIN;
        };
    }
}
