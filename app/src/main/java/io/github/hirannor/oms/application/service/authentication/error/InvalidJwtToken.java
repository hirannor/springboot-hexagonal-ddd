package io.github.hirannor.oms.application.service.authentication.error;

public class InvalidJwtToken extends AuthenticationFailed {
    public InvalidJwtToken(final String message) {
        super(message);
    }
}
