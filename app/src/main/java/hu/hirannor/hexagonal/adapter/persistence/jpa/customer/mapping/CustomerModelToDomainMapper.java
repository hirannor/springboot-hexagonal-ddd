package hu.hirannor.hexagonal.adapter.persistence.jpa.customer.mapping;

import hu.hirannor.hexagonal.adapter.persistence.jpa.customer.model.*;
import hu.hirannor.hexagonal.domain.CustomerId;
import hu.hirannor.hexagonal.domain.EmailAddress;
import hu.hirannor.hexagonal.domain.customer.*;

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

        return Customer.from(
                CustomerId.from(model.getCustomerId()),
                FullName.from(
                    model.getFirstName(),
                    model.getLastName()
                ),
                model.getBirthDate(),
                mapGenderModelToDomain.apply(model.getGender()),
                Address.from(
                        mapCountryModelToDomain.apply(model.getCountry()),
                        model.getCity(),
                        PostalCode.from(model.getPostalCode()),
                        model.getStreetAddress()
                ),
                EmailAddress.from(model.getEmailAddress())
        );
    }

}
