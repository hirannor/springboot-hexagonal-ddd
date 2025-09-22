package io.github.hirannor.oms.adapter.web.rest.authentication;

import io.github.hirannor.oms.adapter.web.rest.authentication.api.AuthApi;
import io.github.hirannor.oms.adapter.web.rest.authentication.mapping.AuthenticationModelToCommandMapper;
import io.github.hirannor.oms.adapter.web.rest.authentication.mapping.AuthenticationResultToModelMapper;
import io.github.hirannor.oms.adapter.web.rest.authentication.model.AuthenticateModel;
import io.github.hirannor.oms.adapter.web.rest.authentication.model.AuthenticationResultModel;
import io.github.hirannor.oms.adapter.web.rest.authentication.model.RefreshRequestModel;
import io.github.hirannor.oms.application.usecase.customer.Authenticating;
import io.github.hirannor.oms.application.usecase.customer.RefreshAuthentication;
import io.github.hirannor.oms.domain.authentication.AttemptAuthentication;
import io.github.hirannor.oms.domain.authentication.AuthenticationResult;
import io.github.hirannor.oms.domain.authentication.RefreshToken;
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

    private final Function<AuthenticateModel, AttemptAuthentication> mapModelToCommand;
    private final Function<AuthenticationResult, AuthenticationResultModel> mapResultToModel;

    private final Authenticating user;
    private final RefreshAuthentication token;

    @Autowired
    AuthenticationController(final Authenticating user, RefreshAuthentication token) {
        this(
                user,
                new AuthenticationModelToCommandMapper(),
                new AuthenticationResultToModelMapper(), token
        );
    }


    AuthenticationController(final Authenticating user,
                             final Function<AuthenticateModel, AttemptAuthentication> mapModelToCommand,
                             final Function<AuthenticationResult, AuthenticationResultModel> mapResultToModel,
                             final RefreshAuthentication token) {
        this.user = user;
        this.mapModelToCommand = mapModelToCommand;
        this.mapResultToModel = mapResultToModel;
        this.token = token;
    }

    @Override
    public ResponseEntity<AuthenticationResultModel> authenticate(final AuthenticateModel authenticateModel) {
        final AttemptAuthentication command = mapModelToCommand.apply(authenticateModel);
        final AuthenticationResult result = user.authenticate(command);

        return ResponseEntity.ok(mapResultToModel.apply(result));
    }

    @Override
    public ResponseEntity<AuthenticationResultModel> refreshToken(final RefreshRequestModel model) {
        final AuthenticationResult result = token.refresh(RefreshToken.issue(model.getRefreshToken()));
        return ResponseEntity.ok(mapResultToModel.apply(result));
    }
}
