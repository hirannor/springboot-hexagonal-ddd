package com.hirannor.hexagonal.application.usecase;

import com.hirannor.hexagonal.domain.customer.ChangeCustomerDetails;
import com.hirannor.hexagonal.domain.customer.Customer;

/**
 * Customer related use case for changing customer related details.
 *
 * @author Mate Karolyi
 */
public interface CustomerModification {

    /**
     * Changes customer details based on the incoming {@link ChangeCustomerDetails} command.
     *
     * @param cmd {@link ChangeCustomerDetails} including the modifications
     * @return {@link Customer} customer with modified details
     */
    Customer change(ChangeCustomerDetails cmd);
}
