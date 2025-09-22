package io.github.hirannor.oms.adapter.persistence.jpa.authentication;

import io.github.hirannor.oms.adapter.persistence.jpa.authentication.role.PermissionRoleModel;
import io.github.hirannor.oms.adapter.persistence.jpa.authentication.role.RoleMappingFactory;
import io.github.hirannor.oms.adapter.persistence.jpa.authentication.role.RoleModel;
import io.github.hirannor.oms.adapter.persistence.jpa.authentication.role.RoleSpringDataJpaRepository;
import io.github.hirannor.oms.domain.authentication.AuthUser;
import io.github.hirannor.oms.domain.authentication.AuthenticationRepository;
import io.github.hirannor.oms.domain.authentication.Role;
import io.github.hirannor.oms.domain.core.valueobject.EmailAddress;
import io.github.hirannor.oms.infrastructure.adapter.DrivenAdapter;
import io.github.hirannor.oms.infrastructure.adapter.PersistenceAdapter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@DrivenAdapter
@PersistenceAdapter
class AuthenticationJpaRepository implements AuthenticationRepository {

    private static final Logger LOGGER = LogManager.getLogger(AuthenticationJpaRepository.class);

    private final Function<AuthUser, AuthUserModel> mapAuthUserToModel;
    private final Function<AuthUserModel, AuthUser> mapAuthUserModelToValueObject;

    private final Function<Role, PermissionRoleModel> mapRoleToModel;

    private final AuthenticationSpringDataJpaRepository authentications;
    private final RoleSpringDataJpaRepository roles;

    @Autowired
    AuthenticationJpaRepository(final AuthenticationSpringDataJpaRepository authentications,
                                final RoleSpringDataJpaRepository roles) {
        this(
                authentications,
                roles,
                new AuthUserToModelMapper(),
                new AuthUserModelToValueObjectMapper(),
                RoleMappingFactory.createRoleModelToRoleMapper());
    }

    AuthenticationJpaRepository(final AuthenticationSpringDataJpaRepository authentications,
                                final RoleSpringDataJpaRepository roles,
                                final Function<AuthUser, AuthUserModel> mapAuthUserToModel,
                                final Function<AuthUserModel, AuthUser> mapAuthUserModelToValueObject,
                                final Function<Role, PermissionRoleModel> mapRoleToModel) {
        this.authentications = authentications;
        this.roles = roles;
        this.mapAuthUserToModel = mapAuthUserToModel;
        this.mapAuthUserModelToValueObject = mapAuthUserModelToValueObject;
        this.mapRoleToModel = mapRoleToModel;
    }

    @Override
    public void save(final AuthUser auth) {
        if (auth == null) throw new IllegalArgumentException("auth cannot be null");

        final AuthUserModel toPersist = mapAuthUserToModel.apply(auth);

        final Set<Role> rolesToSave = auth.roles().isEmpty()
                ? Set.of(Role.CUSTOMER)
                : auth.roles();

        final Set<RoleModel> persistedRoles = rolesToSave
                .stream()
                .map(this::mapRoleToPersisted)
                .collect(Collectors.toSet());

        toPersist.setRoles(persistedRoles);

        authentications.save(toPersist);
    }

    @Override
    public Optional<AuthUser> findByEmail(final EmailAddress email) {
        if (email == null) throw new IllegalArgumentException("email cannot be null");

        return authentications.findByEmailAddress(email.value())
                .map(mapAuthUserModelToValueObject);
    }

    private RoleModel mapRoleToPersisted(Role role) {
        final PermissionRoleModel permission = mapRoleToModel.apply(role);
        return roles.findByName(permission.dbRepresentation())
                .orElseThrow(failBecauseRoleNotFoundBy(permission));
    }

    private Supplier<IllegalStateException> failBecauseRoleNotFoundBy(PermissionRoleModel permission) {
        return () -> new IllegalStateException("Role not found: " + permission);
    }
}
