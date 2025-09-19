package hu.hirannor.hexagonal.domain.authentication;

import hu.hirannor.hexagonal.domain.core.valueobject.EmailAddress;
import java.util.Optional;

public interface AuthenticationRepository {
    void save(AuthUser auth);
    Optional<AuthUser> findByEmail(EmailAddress email);
}
