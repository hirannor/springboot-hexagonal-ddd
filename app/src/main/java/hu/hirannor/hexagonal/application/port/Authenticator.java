package hu.hirannor.hexagonal.application.port;

import hu.hirannor.hexagonal.domain.authentication.AuthUser;
import hu.hirannor.hexagonal.domain.authentication.AuthenticationResult;

public interface Authenticator {
    AuthenticationResult authenticate(AuthUser cmd);
    AuthUser validateToken(String token);
    void register(AuthUser cmd);
}
