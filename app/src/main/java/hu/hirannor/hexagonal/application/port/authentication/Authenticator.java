package hu.hirannor.hexagonal.application.port.authentication;

import hu.hirannor.hexagonal.domain.authentication.AuthUser;
import hu.hirannor.hexagonal.domain.authentication.AuthenticationResult;

public interface Authenticator {
    AuthenticationResult authenticate(AuthUser user);
    AuthUser validateToken(String token);
    void register(AuthUser user);
}
