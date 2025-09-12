package hu.hirannor.hexagonal.adapter.persistence.jpa.authentication;

import hu.hirannor.hexagonal.domain.authentication.AuthUser;

import java.util.function.Function;

class AuthUserToModelMapper implements Function<AuthUser, AuthUserModel> {

    AuthUserToModelMapper() {
    }
    
    @Override
    public AuthUserModel apply(final AuthUser valueObject) {
        if (valueObject == null) return null;

        final AuthUserModel model = new AuthUserModel();
        model.setPassword(valueObject.password().value());
        model.setEmailAddress(valueObject.emailAddress().value());

        return model;
    }
}
