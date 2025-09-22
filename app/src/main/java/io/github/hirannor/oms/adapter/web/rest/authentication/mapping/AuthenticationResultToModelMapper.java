package io.github.hirannor.oms.adapter.web.rest.authentication.mapping;

import io.github.hirannor.oms.adapter.web.rest.authentication.model.AuthenticationResultModel;
import io.github.hirannor.oms.domain.authentication.AuthenticationResult;

import java.util.function.Function;

public class AuthenticationResultToModelMapper implements Function<AuthenticationResult, AuthenticationResultModel> {

    public AuthenticationResultToModelMapper() {
    }

    @Override
    public AuthenticationResultModel apply(final AuthenticationResult result) {
        if (result == null) return null;

        return new AuthenticationResultModel()
                .emailAddress(result.emailAddress().value())
                .accessToken(result.accessToken())
                .refreshToken(result.refreshToken());
    }
}
