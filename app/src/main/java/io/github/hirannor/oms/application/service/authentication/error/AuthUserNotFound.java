package io.github.hirannor.oms.application.service.authentication.error;

public class AuthUserNotFound extends AuthenticationFailed {
    public AuthUserNotFound(final String message) {
        super(message);
    }
}
