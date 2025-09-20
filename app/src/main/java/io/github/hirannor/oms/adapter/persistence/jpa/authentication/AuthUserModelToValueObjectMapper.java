package io.github.hirannor.oms.adapter.persistence.jpa.authentication;

import io.github.hirannor.oms.adapter.persistence.jpa.authentication.role.PermissionRoleModel;
import io.github.hirannor.oms.adapter.persistence.jpa.authentication.role.RoleMappingFactory;
import io.github.hirannor.oms.adapter.persistence.jpa.authentication.role.RoleModel;
import io.github.hirannor.oms.domain.authentication.AuthUser;
import io.github.hirannor.oms.domain.authentication.Password;
import io.github.hirannor.oms.domain.authentication.Role;
import io.github.hirannor.oms.domain.core.valueobject.EmailAddress;

import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

class AuthUserModelToValueObjectMapper implements Function<AuthUserModel, AuthUser> {

    private final Function<PermissionRoleModel, Role> mapRoleModelToRole;

    AuthUserModelToValueObjectMapper() {
        this.mapRoleModelToRole = RoleMappingFactory.createPermissionRoleModelToRoleMapper();
    }

    @Override
    public AuthUser apply(final AuthUserModel model) {
        if (model == null) return null;

        final Set<Role> roles = model.getRoles()
                .stream()
                .map(extractPermission().andThen(mapRoleModelToRole))
                .collect(Collectors.toSet());

        return AuthUser.of(
                EmailAddress.from(model.getEmailAddress()),
                Password.from(model.getPassword()),
                roles
        );
    }

    private Function<RoleModel, PermissionRoleModel> extractPermission() {
        return roleModel -> PermissionRoleModel.from(roleModel.getName());
    }
}
