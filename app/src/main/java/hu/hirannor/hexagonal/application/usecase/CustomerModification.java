package hu.hirannor.hexagonal.application.usecase;

import hu.hirannor.hexagonal.domain.customer.Customer;
import hu.hirannor.hexagonal.domain.customer.command.ChangePersonalDetails;

/**
 * Customer related use case for changing customer related details.
 *
 * @author Mate Karolyi
 */
public interface CustomerModification {

    /**
     * Changes customer details based on the incoming {@link ChangePersonalDetails} command.
     *
     * @param cmd {@link ChangePersonalDetails} including the modifications
     * @return {@link Customer} customer with modified details
     */
    Customer changeBy(ChangePersonalDetails cmd);
}
