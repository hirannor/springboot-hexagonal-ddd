package io.github.hirannor.oms.domain.authentication;

import io.github.hirannor.oms.domain.core.valueobject.EmailAddress;

public record AuthenticationResult(EmailAddress emailAddress, String token) {
    public static AuthenticationResult from(final EmailAddress emailAddress, final String token) {
        return new AuthenticationResult(emailAddress, token);
    }
}
