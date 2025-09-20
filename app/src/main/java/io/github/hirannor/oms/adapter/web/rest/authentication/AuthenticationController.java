package io.github.hirannor.oms.adapter.web.rest.authentication;

import io.github.hirannor.oms.adapter.web.rest.authentication.api.AuthApi;
import io.github.hirannor.oms.adapter.web.rest.authentication.mapping.AuthenticationModelToCommandMapper;
import io.github.hirannor.oms.adapter.web.rest.authentication.mapping.AuthenticationResultToModelMapper;
import io.github.hirannor.oms.adapter.web.rest.authentication.model.AuthenticateModel;
import io.github.hirannor.oms.adapter.web.rest.authentication.model.AuthenticationResultModel;
import io.github.hirannor.oms.application.usecase.customer.Authenticating;
import io.github.hirannor.oms.domain.authentication.AuthenticationResult;
import io.github.hirannor.oms.domain.authentication.DoAuthenticate;
import io.github.hirannor.oms.infrastructure.adapter.DriverAdapter;
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
