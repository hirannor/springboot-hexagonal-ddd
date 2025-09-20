package io.github.hirannor.oms.application.port.authentication;

import io.github.hirannor.oms.domain.authentication.AuthUser;
import io.github.hirannor.oms.domain.authentication.AuthenticationResult;

public interface Authenticator {
    AuthenticationResult authenticate(AuthUser user);
    AuthUser validateToken(String token);
    void register(AuthUser user);
}
