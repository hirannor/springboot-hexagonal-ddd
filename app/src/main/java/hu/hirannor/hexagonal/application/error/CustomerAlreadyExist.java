package hu.hirannor.hexagonal.application.error;

/**
 * Error is throw when customer is already exist
 *
 * @author Mate Karolyi
 */
public class CustomerAlreadyExist extends RuntimeException {

    /**
     * Default constructor
     *
     * @param message {@link String} error message
     */
    public CustomerAlreadyExist(final String message) {
        super(message);
    }

}
