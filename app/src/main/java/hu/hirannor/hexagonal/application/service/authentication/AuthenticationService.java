package hu.hirannor.hexagonal.application.service.authentication;

import hu.hirannor.hexagonal.application.port.authentication.Authenticator;
import hu.hirannor.hexagonal.application.usecase.customer.Authenticating;
import hu.hirannor.hexagonal.domain.authentication.AuthUser;
import hu.hirannor.hexagonal.domain.authentication.AuthenticationResult;
import hu.hirannor.hexagonal.domain.authentication.DoAuthenticate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Function;

@Service
@Transactional(
    propagation = Propagation.REQUIRES_NEW,
    isolation = Isolation.REPEATABLE_READ
)
class AuthenticationService implements Authenticating {

    private static final Logger LOGGER = LogManager.getLogger(
        AuthenticationService.class
    );

    private final Function<DoAuthenticate, AuthUser> mapCommandToUser;

    private final Authenticator authenticator;

    @Autowired
    AuthenticationService(final Authenticator authenticator) {
        this(authenticator, new DoAuthenticationToAuthUserMapper());
    }

    AuthenticationService(final Authenticator authenticator, final Function<DoAuthenticate, AuthUser> mapCommandToUser) {
        this.authenticator = authenticator;
        this.mapCommandToUser = mapCommandToUser;
    }

    @Override
    public AuthenticationResult authenticate(final DoAuthenticate cmd) {
        if (cmd == null) throw new IllegalArgumentException("DoAuthenticate cannot be null");

        LOGGER.info("Attempting to authenticate: {}", cmd.emailAddress().asText());
        final AuthUser authUser = mapCommandToUser.apply(cmd);

        final AuthenticationResult result = authenticator.authenticate(authUser);
        LOGGER.info("Authentication was successful for: {} ", cmd.emailAddress().asText());

        return result;
    }
}
