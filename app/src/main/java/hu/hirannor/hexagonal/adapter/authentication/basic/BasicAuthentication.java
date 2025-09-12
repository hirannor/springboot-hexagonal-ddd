package hu.hirannor.hexagonal.adapter.authentication.basic;

import hu.hirannor.hexagonal.application.port.Authenticator;
import hu.hirannor.hexagonal.domain.authentication.AuthUser;
import hu.hirannor.hexagonal.domain.authentication.AuthenticationResult;
import org.springframework.stereotype.Component;

/**
 * Naive implementation of {@link Authenticator}
 *
 * @author Mate Karolyi
 */
@Component
class BasicAuthentication implements Authenticator {

    @Override
    public AuthenticationResult authenticate(final AuthUser auth) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public AuthUser validateToken(String token) {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public void register(final AuthUser auth) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
