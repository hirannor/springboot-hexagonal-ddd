package io.github.hirannor.oms.adapter.web.rest;

import io.github.hirannor.oms.domain.authentication.Role;

import java.util.function.Function;


class RoleToPermissionRoleModelMapper implements Function<Role, PermissionRoleModel> {

    RoleToPermissionRoleModelMapper() {
    }

    @Override
    public PermissionRoleModel apply(final Role domain) {
        return switch (domain) {
            case ADMIN -> PermissionRoleModel.ADMIN;
            case CUSTOMER -> PermissionRoleModel.CUSTOMER;
        };
    }
}
