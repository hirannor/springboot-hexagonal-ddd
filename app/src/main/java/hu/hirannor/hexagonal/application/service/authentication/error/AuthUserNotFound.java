package hu.hirannor.hexagonal.application.service.authentication.error;

public class AuthUserNotFound extends AuthenticationFailed {
    public AuthUserNotFound(final String message) {
        super(message);
    }
}
