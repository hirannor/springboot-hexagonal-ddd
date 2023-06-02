package hu.hirannor.hexagonal.adapter.web.rest.mapping;

import hu.hirannor.hexagonal.adapter.web.rest.model.*;
import hu.hirannor.hexagonal.domain.customer.*;
import hu.hirannor.hexagonal.domain.customer.command.RegisterCustomer;
import java.util.function.Function;

/**
 * Maps a {@link RegisterCustomerModel} model type to {@link RegisterCustomer} domain type.
 *
 * @author Mate Karolyi
 */
class RegisterCustomerModelToDomainMapper implements Function<RegisterCustomerModel, RegisterCustomer> {

    private final Function<AddressModel, Address> mapAddressModelToDomain;
    private final Function<GenderModel, Gender> mapGenderModelToDomain;

    RegisterCustomerModelToDomainMapper() {
        this(
            new AddressModelToDomainMapper(),
            new GenderModelToDomainMapper()
        );
    }

    RegisterCustomerModelToDomainMapper(final Function<AddressModel, Address> mapAddressModelToDomain,
                                        final Function<GenderModel, Gender> mapGenderModelToDomain) {
        this.mapAddressModelToDomain = mapAddressModelToDomain;
        this.mapGenderModelToDomain = mapGenderModelToDomain;
    }

    @Override
    public RegisterCustomer apply(final RegisterCustomerModel model) {
        if (model == null) return null;

        return RegisterCustomer.issue(
            FullName.from(
                model.getFirstName(),
                model.getLastName()
            ),
            model.getBirthDate(),
            mapGenderModelToDomain.apply(model.getGender()),
            mapAddressModelToDomain.apply(model.getAddress()),
            EmailAddress.from(model.getEmailAddress())
        );
    }

}
