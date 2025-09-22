package io.github.hirannor.oms.application.service.authentication;

import io.github.hirannor.oms.domain.authentication.AttemptAuthentication;
import io.github.hirannor.oms.domain.authentication.AuthUser;

import java.util.function.Function;

class DoAuthenticationToAuthUserMapper implements Function<AttemptAuthentication, AuthUser> {

    DoAuthenticationToAuthUserMapper() {
    }

    @Override
    public AuthUser apply(final AttemptAuthentication cmd) {
        if (cmd == null) throw new IllegalArgumentException("DoAuthenticate cannot be null");

        return AuthUser.of(cmd.emailAddress(), cmd.password(), null);
    }
}
