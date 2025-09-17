package hu.hirannor.hexagonal.application.service.authentication.error;

public class InvalidPassword extends RuntimeException {

    /**
     * Default constructor
     *
     * @param message {@link String} error message
     */
    public InvalidPassword(final String message) {
        super(message);
    }
}
