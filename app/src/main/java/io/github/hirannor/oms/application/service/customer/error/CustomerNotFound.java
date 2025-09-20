package io.github.hirannor.oms.application.service.customer.error;

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
