package hu.hirannor.hexagonal.adapter.persistence.jpa.authentication;

import hu.hirannor.hexagonal.adapter.persistence.jpa.authentication.role.*;
import hu.hirannor.hexagonal.domain.authentication.*;
import hu.hirannor.hexagonal.domain.core.valueobject.EmailAddress;
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
