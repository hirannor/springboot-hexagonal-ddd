package io.github.hirannor.oms.application.usecase.customer;

import io.github.hirannor.oms.domain.customer.Customer;
import io.github.hirannor.oms.domain.customer.command.ChangePersonalDetails;

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
    Customer changePersonalDetailsBy(ChangePersonalDetails cmd);
}
