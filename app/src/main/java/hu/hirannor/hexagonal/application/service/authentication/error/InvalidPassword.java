package hu.hirannor.hexagonal.application.service.authentication.error;

public class InvalidPassword extends AuthenticationFailed {
    public InvalidPassword(final String message) {
        super(message);
    }
}
