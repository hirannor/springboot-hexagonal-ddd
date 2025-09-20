package io.github.hirannor.oms.application.service.authentication;

import io.github.hirannor.oms.domain.authentication.AuthUser;
import io.github.hirannor.oms.domain.authentication.DoAuthenticate;

import java.util.function.Function;

class DoAuthenticationToAuthUserMapper implements Function<DoAuthenticate, AuthUser> {

    DoAuthenticationToAuthUserMapper() {}

    @Override
    public AuthUser apply(final DoAuthenticate cmd) {
        if (cmd == null) throw new IllegalArgumentException("DoAuthenticate cannot be null");

        return AuthUser.of(cmd.emailAddress(), cmd.password(), null);
    }
}
