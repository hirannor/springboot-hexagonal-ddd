package hu.hirannor.hexagonal.adapter.persistence.jpa.authentication;

import hu.hirannor.hexagonal.domain.EmailAddress;
import hu.hirannor.hexagonal.domain.authentication.AuthUser;
import hu.hirannor.hexagonal.domain.authentication.Password;

import java.util.function.Function;

class AuthUserModelToValueObjectMapper implements Function<AuthUserModel, AuthUser> {

    @Override
    public AuthUser apply(final AuthUserModel model) {
        if (model == null) return null;

        return AuthUser.of(EmailAddress.from(model.getEmailAddress()), Password.from(model.getPassword()));
    }
}
