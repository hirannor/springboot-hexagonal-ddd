package hu.hirannor.hexagonal.adapter.web.rest.mapping;

import hu.hirannor.hexagonal.adapter.web.rest.model.*;
import hu.hirannor.hexagonal.domain.customer.*;
import java.util.function.Function;

/**
 * Maps a {@link Customer} domain type to {@link CustomerModel} model type.
 *
 * @author Mate Karolyi
 */
class CustomerToModelMapper implements Function<Customer, CustomerModel> {

    private final Function<Gender, GenderModel> mapGenderToModel;
    private final Function<Address, AddressModel> mapAddressToModel;

    CustomerToModelMapper() {
        this(
            new GenderToModelMapper(),
            new AddressToModelMapper()
        );
    }

    CustomerToModelMapper(final Function<Gender, GenderModel> mapGenderToModel,
                          final Function<Address, AddressModel> mapAddressToModel) {

        this.mapGenderToModel = mapGenderToModel;
        this.mapAddressToModel = mapAddressToModel;
    }

    @Override
    public CustomerModel apply(final Customer customer) {
        if (customer == null) return null;

        return new CustomerModel()
            .customerId(customer.customerId().value())
            .firstName(customer.fullName().firstName())
            .lastName(customer.fullName().lastName())
            .birthDate(customer.birthDate())
            .gender(mapGenderToModel.apply(customer.gender()))
            .address(mapAddressToModel.apply(customer.address()))
            .emailAddress(customer.emailAddress().value());
    }

}
