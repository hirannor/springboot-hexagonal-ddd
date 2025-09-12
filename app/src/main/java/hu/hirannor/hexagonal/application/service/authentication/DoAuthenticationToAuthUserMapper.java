package hu.hirannor.hexagonal.application.service.authentication;

import hu.hirannor.hexagonal.domain.authentication.AuthUser;
import hu.hirannor.hexagonal.domain.authentication.DoAuthenticate;

import java.util.function.Function;

class DoAuthenticationToAuthUserMapper implements Function<DoAuthenticate, AuthUser> {

    DoAuthenticationToAuthUserMapper() {}

    @Override
    public AuthUser apply(final DoAuthenticate cmd) {
        if (cmd == null) throw new IllegalArgumentException("DoAuthenticate cannot be null");

        return AuthUser.of(cmd.emailAddress(), cmd.password(), null);
    }
}
