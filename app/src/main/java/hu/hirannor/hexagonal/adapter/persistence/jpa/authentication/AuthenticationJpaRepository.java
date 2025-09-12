package hu.hirannor.hexagonal.adapter.persistence.jpa.authentication;

import hu.hirannor.hexagonal.domain.EmailAddress;
import hu.hirannor.hexagonal.domain.authentication.AuthUser;
import hu.hirannor.hexagonal.domain.authentication.AuthenticationRepository;
import hu.hirannor.hexagonal.infrastructure.adapter.DrivenAdapter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.function.Function;

@Repository
@Transactional(
        propagation = Propagation.MANDATORY,
        isolation = Isolation.REPEATABLE_READ
)
@DrivenAdapter
class AuthenticationJpaRepository implements AuthenticationRepository {

    private static final Logger LOGGER = LogManager.getLogger(
        AuthenticationJpaRepository.class
    );

    private final Function<AuthUser, AuthUserModel> mapAuthUserToModel;
    private final Function<AuthUserModel, AuthUser> mapAuthUserModelToValueObject;

    private final AuthenticationSpringDataJpaRepository authentications;

    @Autowired
    AuthenticationJpaRepository(final AuthenticationSpringDataJpaRepository authentications) {
        this(authentications, new AuthUserToModelMapper(), new AuthUserModelToValueObjectMapper());
    }

    AuthenticationJpaRepository(final AuthenticationSpringDataJpaRepository authentications,
                                final Function<AuthUser, AuthUserModel> mapAuthUserToModel,
                                final Function<AuthUserModel, AuthUser> mapAuthUserModelToValueObject) {
        this.authentications = authentications;
        this.mapAuthUserToModel = mapAuthUserToModel;
        this.mapAuthUserModelToValueObject = mapAuthUserModelToValueObject;
    }

    @Override
    public void save(final AuthUser auth) {
        if (auth == null) throw new IllegalArgumentException("auth cannot be null");

        final AuthUserModel toPersist = mapAuthUserToModel.apply(auth);

        authentications.save(toPersist);
    }

    @Override
    public Optional<AuthUser> findByEmail(final EmailAddress email) {
        if (email == null) throw new IllegalArgumentException("email cannot be null");

        return authentications.findByEmailAddress(email.value())
                .map(mapAuthUserModelToValueObject);
    }
}
