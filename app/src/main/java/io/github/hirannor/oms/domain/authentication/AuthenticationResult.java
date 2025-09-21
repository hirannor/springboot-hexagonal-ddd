package io.github.hirannor.oms.domain.authentication;

import io.github.hirannor.oms.domain.core.valueobject.EmailAddress;

public record AuthenticationResult(EmailAddress emailAddress, String accessToken, String refreshToken) {
    public static AuthenticationResult from(final EmailAddress emailAddress, final String accessToken, final String refreshToken) {
        return new AuthenticationResult(emailAddress, accessToken, refreshToken);
    }
}
