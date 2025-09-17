package hu.hirannor.hexagonal.adapter.persistence.jpa.customer.mapping;

import hu.hirannor.hexagonal.adapter.persistence.jpa.customer.model.CountryModel;
import hu.hirannor.hexagonal.adapter.persistence.jpa.customer.model.CustomerView;
import hu.hirannor.hexagonal.adapter.persistence.jpa.customer.model.GenderModel;
import hu.hirannor.hexagonal.domain.core.valueobject.CustomerId;
import hu.hirannor.hexagonal.domain.core.valueobject.EmailAddress;
import hu.hirannor.hexagonal.domain.customer.*;

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

