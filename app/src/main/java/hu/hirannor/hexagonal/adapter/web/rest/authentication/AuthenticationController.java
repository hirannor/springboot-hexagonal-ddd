package hu.hirannor.hexagonal.adapter.web.rest.authentication;

import hu.hirannor.hexagonal.adapter.web.rest.authentication.api.AuthApi;
import hu.hirannor.hexagonal.adapter.web.rest.authentication.model.AuthenticateModel;
import hu.hirannor.hexagonal.adapter.web.rest.authentication.model.AuthenticationResultModel;
import hu.hirannor.hexagonal.application.usecase.Authenticating;
import hu.hirannor.hexagonal.domain.authentication.AuthenticationResult;
import hu.hirannor.hexagonal.domain.authentication.DoAuthenticate;
import hu.hirannor.hexagonal.infrastructure.adapter.DriverAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.function.Function;

@RestController
@RequestMapping(value = "/api")
@DriverAdapter
class AuthenticationController implements AuthApi {

    private final Function<AuthenticateModel, DoAuthenticate> mapModelToCommand;
    private final Function<AuthenticationResult, AuthenticationResultModel> mapResultToModel;

    private final Authenticating user;

    @Autowired
    AuthenticationController(final Authenticating user) {
      this(
          user,
          new AuthenticationModelToCommandMapper(),
          new AuthenticationResultToModelMapper()
      );
    }


    AuthenticationController(final Authenticating user,
                             final Function<AuthenticateModel, DoAuthenticate> mapModelToCommand,
                             final Function<AuthenticationResult, AuthenticationResultModel> mapResultToModel) {
        this.user = user;
        this.mapModelToCommand = mapModelToCommand;
        this.mapResultToModel = mapResultToModel;
    }

    @Override
    public ResponseEntity<AuthenticationResultModel> authenticate(final AuthenticateModel authenticateModel) {
        final DoAuthenticate command = mapModelToCommand.apply(authenticateModel);
        final AuthenticationResult result = user.authenticate(command);

        return ResponseEntity.ok(mapResultToModel.apply(result));
    }
}
