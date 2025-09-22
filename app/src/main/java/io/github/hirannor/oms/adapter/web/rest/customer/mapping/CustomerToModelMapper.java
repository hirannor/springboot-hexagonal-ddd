package io.github.hirannor.oms.adapter.web.rest.customer.mapping;

import io.github.hirannor.oms.adapter.web.rest.customer.model.AddressModel;
import io.github.hirannor.oms.adapter.web.rest.customer.model.CustomerModel;
import io.github.hirannor.oms.adapter.web.rest.customer.model.GenderModel;
import io.github.hirannor.oms.domain.customer.*;

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

        final CustomerModel model = new CustomerModel()
                .customerId(customer.id().value())
                .emailAddress(customer.emailAddress().value());

        Optional.ofNullable(customer.firstName())
                .map(FirstName::value)
                .ifPresent(model::setFirstName);

        Optional.ofNullable(customer.lastName())
                .map(LastName::value)
                .ifPresent(model::setLastName);

        Optional.ofNullable(customer.birthDate())
                .ifPresent(model::setBirthDate);

        Optional.ofNullable(customer.gender())
                .map(mapGenderToModel)
                .ifPresent(model::setGender);

        Optional.ofNullable(customer.address())
                .map(mapAddressToModel)
                .ifPresent(model::setAddress);

        return model;
    }

}
