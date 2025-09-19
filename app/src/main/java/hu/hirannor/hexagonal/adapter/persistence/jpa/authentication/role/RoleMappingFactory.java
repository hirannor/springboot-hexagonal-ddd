package hu.hirannor.hexagonal.adapter.persistence.jpa.authentication.role;

import hu.hirannor.hexagonal.domain.authentication.Role;
import java.util.function.Function;

public interface RoleMappingFactory {

    static Function<PermissionRoleModel, Role> createPermissionRoleModelToRoleMapper() {
        return new PermissionRoleModelToRole();
    }

    static Function<Role, PermissionRoleModel> createRoleModelToRoleMapper() {
        return new RoleToPermissionRoleModel();
    }
}
