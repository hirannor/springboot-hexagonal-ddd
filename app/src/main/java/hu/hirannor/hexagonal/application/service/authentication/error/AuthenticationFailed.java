package hu.hirannor.hexagonal.application.service.authentication.error;

public abstract class AuthenticationFailed extends RuntimeException {
    protected AuthenticationFailed(String message) {
        super(message);
    }
}