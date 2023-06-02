package hu.hirannor.hexagonal.application.error;

/**
 * Error is throw when customer not found during an operation.
 *
 * @author Mate Karolyi
 */
public class CustomerNotFound extends RuntimeException {

    /**
     * Default constructor
     *
     * @param message {@link String} error message
     */
    public CustomerNotFound(final String message) {
        super(message);
    }

}
