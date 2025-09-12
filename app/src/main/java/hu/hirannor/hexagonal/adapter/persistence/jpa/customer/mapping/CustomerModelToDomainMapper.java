package hu.hirannor.hexagonal.adapter.persistence.jpa.customer.mapping;

import hu.hirannor.hexagonal.adapter.persistence.jpa.customer.model.*;
import hu.hirannor.hexagonal.domain.CustomerId;
import hu.hirannor.hexagonal.domain.EmailAddress;
import hu.hirannor.hexagonal.domain.customer.*;

import java.util.Optional;
import java.util.function.Function;

/**
 * Maps a {@link CustomerModel} model type to {@link Customer} domain type.
 *
 * @author Mate Karolyi
 */
class CustomerModelToDomainMapper implements Function<CustomerModel, Customer> {

    private final Function<CountryModel, Country> mapCountryModelToDomain;
    private final Function<GenderModel, Gender> mapGenderModelToDomain;

    CustomerModelToDomainMapper() {
        this(
            new CountryModelToDomainMapper(),
            new GenderModelToDomainMapper()
        );
    }

    CustomerModelToDomainMapper(final Function<CountryModel, Country> mapCountryModelToDomain,
                                final Function<GenderModel, Gender> mapGenderModelToDomain) {
        this.mapCountryModelToDomain = mapCountryModelToDomain;
        this.mapGenderModelToDomain = mapGenderModelToDomain;
    }

    @Override
    public Customer apply(final CustomerModel model) {
        if (model == null) return null;

        final CustomerBuilder builder = CustomerBuilder.empty()
                .customerId(CustomerId.from(model.getCustomerId()))
                .emailAddress(EmailAddress.from(model.getEmailAddress()));

        Optional.ofNullable(model.getGender())
                .map(mapGenderModelToDomain)
                .ifPresent(builder::gender);

        final FullName name = FullName.from(
                Optional.ofNullable(model.getFirstName()).orElse(""),
                Optional.ofNullable(model.getLastName()).orElse("")
        );

        builder.fullName(name);

        final Address address = Address.from(
                Optional.ofNullable(model.getCountry()).map(mapCountryModelToDomain).orElse(Country.HUNGARY),
                Optional.ofNullable(model.getCity()).orElse(""),
                Optional.ofNullable(model.getPostalCode()).map(PostalCode::from).orElse(PostalCode.empty()),
                Optional.ofNullable(model.getStreetAddress()).orElse("")
        );

        builder.address(address);

        Optional.ofNullable(model.getBirthDate())
                .ifPresent(builder::birthDate);

        return builder.createCustomer();
    }

}
