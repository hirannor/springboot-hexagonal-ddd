package hu.hirannor.hexagonal.adapter.web.rest.authentication;

import hu.hirannor.hexagonal.adapter.web.rest.authentication.model.AuthenticateModel;
import hu.hirannor.hexagonal.domain.EmailAddress;
import hu.hirannor.hexagonal.domain.authentication.DoAuthenticate;
import hu.hirannor.hexagonal.domain.authentication.Password;

import java.util.function.Function;

class AuthenticationModelToCommandMapper implements Function<AuthenticateModel, DoAuthenticate> {

    AuthenticationModelToCommandMapper() {}

    @Override
    public DoAuthenticate apply(final AuthenticateModel model) {
        if (model == null) throw new IllegalArgumentException("model is null");
        return DoAuthenticate.issue(
            EmailAddress.from(model.getEmailAddress()),
            Password.from(model.getPassword())
        );
    }
}
