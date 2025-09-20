package io.github.hirannor.oms.adapter.web.rest.authentication.mapping;

import io.github.hirannor.oms.adapter.web.rest.authentication.model.AuthenticateModel;
import io.github.hirannor.oms.domain.authentication.DoAuthenticate;
import io.github.hirannor.oms.domain.authentication.Password;
import io.github.hirannor.oms.domain.core.valueobject.EmailAddress;

import java.util.function.Function;

public class AuthenticationModelToCommandMapper implements Function<AuthenticateModel, DoAuthenticate> {

    public AuthenticationModelToCommandMapper() {}

    @Override
    public DoAuthenticate apply(final AuthenticateModel model) {
        if (model == null) throw new IllegalArgumentException("model is null");
        return DoAuthenticate.issue(
            EmailAddress.from(model.getEmailAddress()),
            Password.from(model.getPassword())
        );
    }
}
