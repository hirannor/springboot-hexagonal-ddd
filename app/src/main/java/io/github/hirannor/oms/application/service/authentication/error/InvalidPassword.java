package io.github.hirannor.oms.application.service.authentication.error;

public class InvalidPassword extends AuthenticationFailed {
    public InvalidPassword(final String message) {
        super(message);
    }
}
