package io.github.hirannor.oms.adapter.persistence.jpa.customer.mapping;

import io.github.hirannor.oms.adapter.persistence.jpa.customer.model.CountryModel;
import io.github.hirannor.oms.adapter.persistence.jpa.customer.model.CustomerView;
import io.github.hirannor.oms.adapter.persistence.jpa.customer.model.GenderModel;
import io.github.hirannor.oms.domain.core.valueobject.CustomerId;
import io.github.hirannor.oms.domain.core.valueobject.EmailAddress;
import io.github.hirannor.oms.domain.customer.*;

import java.util.Optional;
import java.util.function.Function;

/**
 * Maps a {@link CustomerView} view notificationType to {@link Customer} domain notificationType.
 *
 * @author Mate Karolyi
 */
class CustomerViewToDomainMapper implements Function<CustomerView, Customer> {

    private final Function<GenderModel, Gender> mapGenderModelToDomain;
    private final Function<CountryModel, Country> mapCountryModelToDomain;

    CustomerViewToDomainMapper() {
        this(
                new GenderModelToDomainMapper(),
                new CountryModelToDomainMapper()
        );
    }

    CustomerViewToDomainMapper(
            final Function<GenderModel, Gender> mapGenderModelToDomain,
            final Function<CountryModel, Country> mapCountryModelToDomain) {
        this.mapGenderModelToDomain = mapGenderModelToDomain;
        this.mapCountryModelToDomain = mapCountryModelToDomain;
    }

    @Override
    public Customer apply(final CustomerView view) {
        if (view == null) return null;

        final CustomerBuilder builder = CustomerBuilder.empty()
                .customerId(CustomerId.from(view.getCustomerId()))
                .emailAddress(EmailAddress.from(view.getEmailAddress()));

        Optional.ofNullable(view.getGender())
                .map(mapGenderModelToDomain)
                .ifPresent(builder::gender);

        Optional.ofNullable(view.getFirstName())
                .map(FirstName::from)
                .ifPresent(builder::firstName);

        Optional.ofNullable(view.getLastName())
                .map(LastName::from)
                .ifPresent(builder::lastName);

        if (isCompleteAddress(view)) {
            Address address = Address.from(
                    mapCountryModelToDomain.apply(view.getCountry()),
                    view.getCity(),
                    PostalCode.from(view.getPostalCode()),
                    view.getStreetAddress()
            );
            builder.address(address);
        }

        Optional.ofNullable(view.getBirthDate())
                .ifPresent(builder::birthDate);

        return builder.createCustomer();

    }

    private boolean isCompleteAddress(CustomerView view) {
        return view.getCountry() != null
                && view.getPostalCode() != null
                && view.getCity() != null && !view.getCity().isBlank()
                && view.getStreetAddress() != null && !view.getStreetAddress().isBlank();
    }
}

