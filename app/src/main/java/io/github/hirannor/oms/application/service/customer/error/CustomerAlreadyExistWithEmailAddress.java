package io.github.hirannor.oms.application.service.customer.error;

/**
 * Error is throw when customer is already exist
 *
 * @author Mate Karolyi
 */
public class CustomerAlreadyExistWithEmailAddress extends RuntimeException {

    /**
     * Default constructor
     *
     * @param message {@link String} error message
     */
    public CustomerAlreadyExistWithEmailAddress(final String message) {
        super(message);
    }

}
