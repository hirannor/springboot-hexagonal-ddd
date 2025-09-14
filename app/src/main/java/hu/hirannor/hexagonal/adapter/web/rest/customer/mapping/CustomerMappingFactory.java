package hu.hirannor.hexagonal.adapter.web.rest.customer.mapping;

import hu.hirannor.hexagonal.adapter.web.rest.customer.model.AddressModel;
import hu.hirannor.hexagonal.adapter.web.rest.customer.model.ChangePersonalDetailsModel;
import hu.hirannor.hexagonal.adapter.web.rest.customer.model.CustomerModel;
import hu.hirannor.hexagonal.adapter.web.rest.customer.model.GenderModel;
import hu.hirannor.hexagonal.domain.customer.Address;
import hu.hirannor.hexagonal.domain.customer.Customer;
import hu.hirannor.hexagonal.domain.customer.Gender;
import hu.hirannor.hexagonal.domain.customer.command.ChangePersonalDetails;

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
     * Create an instance of {@link GenderModelToDomainMapper}, which maps a {@link GenderModel} model notificationType
     * to a {@link Gender} domain notificationType.
     *
     * @return an instance of {@link GenderModelToDomainMapper}
     */
    static Function<GenderModel, Gender> createGenderModelToDomainMapper() {
        return new GenderModelToDomainMapper();
    }

    /**
     * Create an instance of {@link AddressModelToDomainMapper}, which maps a {@link AddressModel} model notificationType
     * to a {@link Address} domain notificationType.
     *
     * @return an instance of {@link AddressModelToDomainMapper}
     */
    static Function<AddressModel, Address> createAddressModelToAddressMapper() {
        return new AddressModelToDomainMapper();
    }

    /**
     * Create an instance of {@link ChangeCustomerDetailsModelToDomainMapper},
     * which maps a {@link ChangePersonalDetailsModel} model notificationType
     * to a {@link ChangePersonalDetails} domain notificationType.
     *
     * @return an instance of {@link ChangeCustomerDetailsModelToDomainMapper}
     */
    static Function<ChangePersonalDetailsModel, ChangePersonalDetails> createChangePersonalDetailsModelToDomainMapper(
            final String customerId) {
        return new ChangeCustomerDetailsModelToDomainMapper(customerId);
    }

}
