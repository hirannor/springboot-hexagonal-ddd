package io.github.hirannor.oms.adapter.persistence.jpa.authentication.role;

import io.github.hirannor.oms.domain.authentication.Role;

import java.util.function.Function;

public interface RoleMappingFactory {

    static Function<PermissionRoleModel, Role> createPermissionRoleModelToRoleMapper() {
        return new PermissionRoleModelToRole();
    }

    static Function<Role, PermissionRoleModel> createRoleModelToRoleMapper() {
        return new RoleToPermissionRoleModel();
    }
}
