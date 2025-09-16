package hu.hirannor.hexagonal.adapter.web.rest.customer.mapping;

import hu.hirannor.hexagonal.adapter.web.rest.customer.model.AddressModel;
import hu.hirannor.hexagonal.adapter.web.rest.customer.model.CustomerModel;
import hu.hirannor.hexagonal.adapter.web.rest.customer.model.GenderModel;
import hu.hirannor.hexagonal.domain.customer.*;

import java.util.Optional;
import java.util.function.Function;

/**
 * Maps a {@link Customer} domain notificationType to {@link CustomerModel} model notificationType.
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

        final CustomerModel model = new CustomerModel();

        Optional.ofNullable(customer.firstName())
            .map(FirstName::value)
            .ifPresent(model::setFirstName);

        Optional.ofNullable(customer.lastName())
            .map(LastName::value)
            .ifPresent(model::setLastName);

        return model
                .customerId(customer.id().value())
                .birthDate(customer.birthDate())
                .gender(mapGenderToModel.apply(customer.gender()))
                .address(mapAddressToModel.apply(customer.address()))
                .emailAddress(customer.emailAddress().value());
    }

}
