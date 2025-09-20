package io.github.hirannor.oms.adapter.web.rest.customer.mapping;

import io.github.hirannor.oms.adapter.web.rest.customer.model.AddressModel;
import io.github.hirannor.oms.adapter.web.rest.customer.model.ChangePersonalDetailsModel;
import io.github.hirannor.oms.adapter.web.rest.customer.model.GenderModel;
import io.github.hirannor.oms.domain.core.valueobject.CustomerId;
import io.github.hirannor.oms.domain.core.valueobject.EmailAddress;
import io.github.hirannor.oms.domain.customer.Address;
import io.github.hirannor.oms.domain.customer.FirstName;
import io.github.hirannor.oms.domain.customer.Gender;
import io.github.hirannor.oms.domain.customer.LastName;
import io.github.hirannor.oms.domain.customer.command.ChangePersonalDetails;

import java.util.function.Function;

/**
 * Maps a {@link ChangePersonalDetailsModel} model notificationType to {@link ChangePersonalDetails} domain notificationType.
 *
 * @author Mate Karolyi
 */
class ChangeCustomerDetailsModelToDomainMapper implements Function<ChangePersonalDetailsModel, ChangePersonalDetails> {

    private final String customerId;

    private final Function<GenderModel, Gender> mapGenderModelToDomain;
    private final Function<AddressModel, Address> mapAddressModelToDomain;

    ChangeCustomerDetailsModelToDomainMapper(final String customerId) {
        this(
            customerId,
            CustomerMappingFactory.createGenderModelToDomainMapper(),
            CustomerMappingFactory.createAddressModelToAddressMapper()
        );
    }

    ChangeCustomerDetailsModelToDomainMapper(final String customerId,
                                             final Function<GenderModel, Gender> mapGenderModelToDomain,
                                             final Function<AddressModel, Address> mapAddressModelToDomain) {
        this.customerId = customerId;
        this.mapGenderModelToDomain = mapGenderModelToDomain;
        this.mapAddressModelToDomain = mapAddressModelToDomain;
    }


    @Override
    public ChangePersonalDetails apply(final ChangePersonalDetailsModel model) {
        if (model == null) return null;

        return new ChangePersonalDetails.Builder()
                .customerId(CustomerId.from(customerId))
                .firstName(FirstName.from(model.getFirstName()))
                .lastName(LastName.from(model.getLastName()))
                .gender(mapGenderModelToDomain.apply(model.getGender()))
                .birthDate(model.getBirthDate())
                .address(mapAddressModelToDomain.apply(model.getAddress()))
                .email(EmailAddress.from(model.getEmailAddress()))
                .assemble();
    }
}
