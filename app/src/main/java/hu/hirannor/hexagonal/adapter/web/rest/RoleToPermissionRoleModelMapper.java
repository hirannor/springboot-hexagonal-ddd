package hu.hirannor.hexagonal.adapter.web.rest;

import hu.hirannor.hexagonal.domain.authentication.Role;
import java.util.function.Function;


class RoleToPermissionRoleModelMapper implements Function<Role, PermissionRoleModel> {

    RoleToPermissionRoleModelMapper() {}

    @Override
    public PermissionRoleModel apply(final Role domain) {
        return switch (domain) {
            case ADMIN -> PermissionRoleModel.ADMIN;
            case CUSTOMER -> PermissionRoleModel.CUSTOMER;
        };
    }
}
