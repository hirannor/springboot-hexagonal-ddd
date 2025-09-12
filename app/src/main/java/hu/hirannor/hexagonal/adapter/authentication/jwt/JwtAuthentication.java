package hu.hirannor.hexagonal.adapter.authentication.jwt;

import hu.hirannor.hexagonal.application.port.Authenticator;
import hu.hirannor.hexagonal.domain.authentication.AuthenticateUser;
import hu.hirannor.hexagonal.domain.authentication.AuthenticatedUser;
import hu.hirannor.hexagonal.domain.authentication.RegisterUser;
import org.springframework.stereotype.Component;

/**
 * Naive implementation of {@link Authenticator}
 *
 * @author Mate Karolyi
 */
@Component
class JwtAuthentication implements Authenticator {
    @Override
    public AuthenticatedUser authenticate(final AuthenticateUser cmd) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void registerUser(RegisterUser cmd) {
        // NOOP
    }
}
