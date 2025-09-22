package io.github.hirannor.oms.adapter.authentication.jwt;

import io.github.hirannor.oms.domain.authentication.Role;

import java.util.function.Function;

class RoleModelToRoleMapper implements Function<RoleModel, Role> {

    RoleModelToRoleMapper() {
    }

    @Override
    public Role apply(final RoleModel model) {
        if (model == null) throw new IllegalArgumentException("model cannot be null");

        return switch (model) {
            case CUSTOMER -> Role.CUSTOMER;
            case ADMIN -> Role.ADMIN;
        };
    }
}
