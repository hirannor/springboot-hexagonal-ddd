package hu.hirannor.hexagonal.adapter.web.rest.authentication;

import hu.hirannor.hexagonal.adapter.web.rest.authentication.api.AuthApi;
import hu.hirannor.hexagonal.adapter.web.rest.authentication.model.AuthenticateModel;
import hu.hirannor.hexagonal.adapter.web.rest.authentication.model.AuthenticationResultModel;
import hu.hirannor.hexagonal.application.usecase.Authenticating;
import hu.hirannor.hexagonal.domain.EmailAddress;
import hu.hirannor.hexagonal.domain.authentication.AuthenticationResult;
import hu.hirannor.hexagonal.domain.authentication.DoAuthenticate;
import hu.hirannor.hexagonal.domain.authentication.Password;
import hu.hirannor.hexagonal.infrastructure.adapter.DriverAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api")
@DriverAdapter
class AuthenticationController implements AuthApi {

    private final Authenticating user;

    @Autowired
    AuthenticationController(final Authenticating user) {
        this.user = user;
    }

    @Override
    public ResponseEntity<AuthenticationResultModel> authenticate(final AuthenticateModel authenticateModel) {
        final AuthenticationResult result = user.authenticate(
                DoAuthenticate.issue(
                    EmailAddress.from(authenticateModel.getEmailAddress()),
                    Password.from(authenticateModel.getPassword())
                )
        );
        return ResponseEntity.ok(
                new AuthenticationResultModel()
                        .emailAddress(result.emailAddress().value())
                        .token(result.token())
        );
    }
}
