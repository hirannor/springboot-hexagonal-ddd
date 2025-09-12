package hu.hirannor.hexagonal.adapter.web.rest.customer.mapping;

import hu.hirannor.hexagonal.adapter.web.rest.customer.model.*;
import hu.hirannor.hexagonal.domain.customer.*;
import hu.hirannor.hexagonal.domain.customer.command.ChangePersonalDetails;

import java.util.function.Function;

/**
 * Accessor factory for customer related mappings.
 *
 * @author Mate Karolyi
 */
public interface CustomerMappingFactory {

    /**
     * Create an instance of {@link CustomerToModelMapper}, which maps a {@link Customer} domain type
     * to a {@link CustomerModel} model type.
     *
     * @return an instance of {@link CustomerToModelMapper}
     */
    static Function<Customer, CustomerModel> createCustomerToModelMapper() {
        return new CustomerToModelMapper();
    }

    /**
     * Create an instance of {@link GenderModelToDomainMapper}, which maps a {@link GenderModel} model type
     * to a {@link Gender} domain type.
     *
     * @return an instance of {@link GenderModelToDomainMapper}
     */
    static Function<GenderModel, Gender> createGenderModelToDomainMapper() {
        return new GenderModelToDomainMapper();
    }

    /**
     * Create an instance of {@link AddressModelToDomainMapper}, which maps a {@link AddressModel} model type
     * to a {@link Address} domain type.
     *
     * @return an instance of {@link AddressModelToDomainMapper}
     */
    static Function<AddressModel, Address> createAddressModelToAddressMapper() {
        return new AddressModelToDomainMapper();
    }

    /**
     * Create an instance of {@link ChangeCustomerDetailsModelToDomainMapper},
     * which maps a {@link ChangePersonalDetailsModel} model type
     * to a {@link ChangePersonalDetails} domain type.
     *
     * @return an instance of {@link ChangeCustomerDetailsModelToDomainMapper}
     */
    static Function<ChangePersonalDetailsModel, ChangePersonalDetails> createChangePersonalDetailsModelToDomainMapper(
            final String customerId) {
        return new ChangeCustomerDetailsModelToDomainMapper(customerId);
    }

}
