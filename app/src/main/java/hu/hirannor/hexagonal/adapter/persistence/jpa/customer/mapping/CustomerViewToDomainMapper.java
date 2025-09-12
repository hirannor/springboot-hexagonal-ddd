package hu.hirannor.hexagonal.adapter.persistence.jpa.customer.mapping;

import hu.hirannor.hexagonal.adapter.persistence.jpa.customer.model.*;
import hu.hirannor.hexagonal.domain.EmailAddress;
import hu.hirannor.hexagonal.domain.customer.*;

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

        return Customer.from(
                CustomerId.from(view.getCustomerId()),
                FullName.from(
                    view.getFirstName(),
                    view.getLastName()
                ),
                view.getBirthDate(),
                mapGenderModelToDomain.apply(view.getGender()),
                Address.from(
                        mapCountryModelToDomain.apply(view.getCountry()),
                        view.getCity(),
                        PostalCode.from(view.getPostalCode()),
                        view.getStreetAddress()
                ),
                EmailAddress.from(view.getEmailAddress())
        );
    }

}

