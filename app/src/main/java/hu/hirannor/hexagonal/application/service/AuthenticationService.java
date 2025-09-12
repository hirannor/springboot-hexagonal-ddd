package hu.hirannor.hexagonal.application.service;

import hu.hirannor.hexagonal.application.port.Authenticator;
import hu.hirannor.hexagonal.application.usecase.Authenticating;
import hu.hirannor.hexagonal.domain.authentication.AuthUser;
import hu.hirannor.hexagonal.domain.authentication.AuthenticationResult;
import hu.hirannor.hexagonal.domain.authentication.DoAuthenticate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(
    propagation = Propagation.REQUIRES_NEW,
    isolation = Isolation.REPEATABLE_READ
)
class AuthenticationService implements Authenticating {

    private final Authenticator authenticator;

    @Autowired
    AuthenticationService(Authenticator authenticator) {
        this.authenticator = authenticator;
    }

    @Override
    public AuthenticationResult authenticate(final DoAuthenticate cmd) {
        return authenticator.authenticate(AuthUser.of(cmd.emailAddress(), cmd.password()));
    }
}
