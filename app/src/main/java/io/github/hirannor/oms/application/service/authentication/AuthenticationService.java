package io.github.hirannor.oms.application.service.authentication;

import io.github.hirannor.oms.application.port.authentication.Authenticator;
import io.github.hirannor.oms.application.usecase.customer.Authenticating;
import io.github.hirannor.oms.application.usecase.customer.RefreshAuthentication;
import io.github.hirannor.oms.domain.authentication.AttemptAuthentication;
import io.github.hirannor.oms.domain.authentication.AuthUser;
import io.github.hirannor.oms.domain.authentication.AuthenticationResult;
import io.github.hirannor.oms.domain.authentication.RefreshToken;
import io.github.hirannor.oms.infrastructure.application.ApplicationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.function.Function;

@ApplicationService
class AuthenticationService implements Authenticating, RefreshAuthentication {

    private static final Logger LOGGER = LogManager.getLogger(
            AuthenticationService.class
    );

    private final Function<AttemptAuthentication, AuthUser> mapCommandToUser;

    private final Authenticator authenticator;

    @Autowired
    AuthenticationService(final Authenticator authenticator) {
        this(authenticator, new DoAuthenticationToAuthUserMapper());
    }

    AuthenticationService(final Authenticator authenticator,
                          final Function<AttemptAuthentication, AuthUser> mapCommandToUser) {
        this.authenticator = authenticator;
        this.mapCommandToUser = mapCommandToUser;
    }

    @Override
    public AuthenticationResult authenticate(final AttemptAuthentication cmd) {
        if (cmd == null) throw new IllegalArgumentException("AttemptAuthentication cannot be null");

        LOGGER.info("Attempting to authenticate with emailAddress={}", cmd.emailAddress().asText());
        final AuthUser authUser = mapCommandToUser.apply(cmd);

        final AuthenticationResult result = authenticator.authenticate(authUser);
        LOGGER.info("Authentication was successful with emailAddress={} ", cmd.emailAddress().asText());

        return result;
    }

    @Override
    public AuthenticationResult refresh(final RefreshToken cmd) {
        if (cmd == null)
            throw new IllegalArgumentException("Refresh token cannot be null or blank");

        LOGGER.info("Attempting to refresh token");

        final AuthenticationResult result = authenticator.refresh(cmd);

        LOGGER.info("Refresh was successful for emailAddress={}", result.emailAddress().asText());

        return result;
    }
}
