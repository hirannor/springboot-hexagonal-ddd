package hu.hirannor.hexagonal.domain.authentication;

import hu.hirannor.hexagonal.domain.EmailAddress;

public record AuthUser(EmailAddress emailAddress, Password password) {

    public AuthUser {
        if (emailAddress == null)
            throw new IllegalArgumentException("EmailAddress cannot be null");
    }

    public static AuthUser of(final EmailAddress emailAddress, final Password password) {
        return new AuthUser(emailAddress, password);
    }
}
