package io.github.hirannor.oms.adapter.persistence.jpa.customer.mapping;

import io.github.hirannor.oms.adapter.persistence.jpa.customer.model.CustomerModel;
import io.github.hirannor.oms.adapter.persistence.jpa.customer.model.CustomerView;
import io.github.hirannor.oms.adapter.persistence.jpa.customer.model.GenderModel;
import io.github.hirannor.oms.domain.customer.Customer;
import io.github.hirannor.oms.domain.customer.Gender;

import java.util.function.Function;

/**
 * Accessor factory for customer related mappings.
 *
 * @author Mate Karolyi
 */
public interface CustomerMappingFactory {

    /**
     * Create an instance of {@link CustomerToModelMapper}, which maps a {@link Customer} domain notificationType
     * to a {@link CustomerModel} model notificationType.
     *
     * @return an instance of {@link CustomerToModelMapper}
     */
    static Function<Customer, CustomerModel> createCustomerToModelMapper() {
        return new CustomerToModelMapper();
    }

    /**
     * Create an instance of {@link CustomerModelToDomainMapper}, which maps a {@link CustomerModel} model notificationType
     * to a {@link Customer} domain notificationType.
     *
     * @return an instance of {@link CustomerModelToDomainMapper}
     */
    static Function<CustomerModel, Customer> createCustomerModelToDomainMapper() {
        return new CustomerModelToDomainMapper();
    }

    /**
     * Create an instance of {@link CustomerViewToDomainMapper}, which maps a {@link CustomerView} view notificationType
     * to a {@link Customer} domain notificationType.
     *
     * @return an instance of {@link CustomerModelToDomainMapper}
     */
    static Function<CustomerView, Customer> createCustomerViewToDomainMapper() {
        return new CustomerViewToDomainMapper();
    }

    /**
     * Create an instance of {@link GenderToModelMapper}, which maps a {@link Gender} domain notificationType
     * to a {@link GenderModel} model notificationType.
     *
     * @return an instance of {@link GenderToModelMapper}
     */
    static Function<Gender, GenderModel> createGenderToModelMapper() {
        return new GenderToModelMapper();
    }

}
