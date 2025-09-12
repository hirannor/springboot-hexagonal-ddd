package hu.hirannor.hexagonal.domain.authentication;

import hu.hirannor.hexagonal.domain.EmailAddress;

public record AuthenticatedUser(EmailAddress emailAddress) {
    public static AuthenticatedUser from(final EmailAddress emailAddress) {
        return new AuthenticatedUser(emailAddress);
    }
}
