package hu.hirannor.hexagonal.adapter.web.rest.authentication.mapping;

import hu.hirannor.hexagonal.adapter.web.rest.authentication.model.AuthenticationResultModel;
import hu.hirannor.hexagonal.domain.authentication.AuthenticationResult;
import java.util.function.Function;

public class AuthenticationResultToModelMapper implements Function<AuthenticationResult, AuthenticationResultModel> {

    public AuthenticationResultToModelMapper() {}

    @Override
    public AuthenticationResultModel apply(final AuthenticationResult result) {
        if (result == null) throw new IllegalArgumentException("result is null");

        return new AuthenticationResultModel()
                .emailAddress(result.emailAddress().value())
                .token(result.token());
    }
}
