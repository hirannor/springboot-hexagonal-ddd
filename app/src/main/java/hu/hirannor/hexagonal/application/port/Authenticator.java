package hu.hirannor.hexagonal.application.port;

import hu.hirannor.hexagonal.domain.authentication.AuthenticateUser;
import hu.hirannor.hexagonal.domain.authentication.AuthenticatedUser;
import hu.hirannor.hexagonal.domain.authentication.RegisterUser;

public interface Authenticator {
    AuthenticatedUser authenticate(AuthenticateUser cmd);
    void registerUser(RegisterUser cmd);
}
