package io.github.hirannor.oms.adapter.web.rest.authentication.mapping;

import io.github.hirannor.oms.adapter.web.rest.authentication.model.AuthenticateModel;
import io.github.hirannor.oms.domain.authentication.AttemptAuthentication;
import io.github.hirannor.oms.domain.authentication.Password;
import io.github.hirannor.oms.domain.core.valueobject.EmailAddress;

import java.util.function.Function;

public class AuthenticationModelToCommandMapper implements Function<AuthenticateModel, AttemptAuthentication> {

    public AuthenticationModelToCommandMapper() {
    }

    @Override
    public AttemptAuthentication apply(final AuthenticateModel model) {
        if (model == null) return null;
        return AttemptAuthentication.issue(
                EmailAddress.from(model.getEmailAddress()),
                Password.from(model.getPassword())
        );
    }
}
