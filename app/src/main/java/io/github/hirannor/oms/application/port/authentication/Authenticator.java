package io.github.hirannor.oms.application.port.authentication;

import io.github.hirannor.oms.domain.authentication.AuthUser;
import io.github.hirannor.oms.domain.authentication.AuthenticationResult;
import io.github.hirannor.oms.domain.authentication.RefreshToken;

public interface Authenticator {
    AuthenticationResult authenticate(AuthUser user);
    AuthenticationResult refresh(RefreshToken refreshToken);
    AuthUser validateAccessToken(String token);
    void register(AuthUser user);
}
