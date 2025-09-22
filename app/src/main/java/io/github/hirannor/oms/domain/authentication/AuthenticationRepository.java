package io.github.hirannor.oms.domain.authentication;

import io.github.hirannor.oms.domain.core.valueobject.EmailAddress;

import java.util.Optional;

public interface AuthenticationRepository {
    void save(AuthUser auth);

    Optional<AuthUser> findByEmail(EmailAddress email);
}
