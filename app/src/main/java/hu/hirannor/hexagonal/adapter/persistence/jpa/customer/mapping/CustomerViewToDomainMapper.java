package hu.hirannor.hexagonal.adapter.persistence.jpa.customer.mapping;

import hu.hirannor.hexagonal.adapter.persistence.jpa.customer.model.*;
import hu.hirannor.hexagonal.domain.CustomerId;
import hu.hirannor.hexagonal.domain.EmailAddress;
import hu.hirannor.hexagonal.domain.customer.*;

import java.util.Optional;
import java.util.function.Function;

/**
 * Maps a {@link CustomerView} view type to {@link Customer} domain type.
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

        final FullName name = FullName.from(
            Optional.ofNullable(view.getFirstName()).orElse(""),
            Optional.ofNullable(view.getLastName()).orElse("")
        );

        builder.fullName(name);

        final Address address = Address.from(
                Optional.ofNullable(view.getCountry()).map(mapCountryModelToDomain).orElse(Country.HUNGARY),
                Optional.ofNullable(view.getCity()).orElse(""),
                Optional.ofNullable(view.getPostalCode()).map(PostalCode::from).orElse(PostalCode.empty()),
                Optional.ofNullable(view.getStreetAddress()).orElse("")
        );

        builder.address(address);

        Optional.ofNullable(view.getBirthDate())
                .ifPresent(builder::birthDate);

        return builder.createCustomer();

    }

}

