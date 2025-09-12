package hu.hirannor.hexagonal.application.usecase;

import hu.hirannor.hexagonal.domain.authentication.AuthenticationResult;
import hu.hirannor.hexagonal.domain.authentication.DoAuthenticate;

public interface Authenticating {
    AuthenticationResult authenticate(DoAuthenticate cmd);
}
