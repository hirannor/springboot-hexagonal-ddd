package com.hirannor.hexagonal.adapter.api.rest;

import com.hirannor.hexagonal.adapter.api.rest.api.CustomersApi;
import com.hirannor.hexagonal.adapter.api.rest.mapping.CustomerMappingFactory;
import com.hirannor.hexagonal.adapter.api.rest.model.*;
import com.hirannor.hexagonal.application.usecase.*;
import com.hirannor.hexagonal.domain.customer.*;
import java.net.URI;
import java.util.List;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * Rest controller implementation of {@link CustomersApi}
 *
 * @author Mate Karolyi
 */
@RestController
class CustomerController implements CustomersApi {

    private static final String BASE_PATH = "/customers/";

    private final Function<Customer, CustomerModel> mapCustomerToModel;
    private final Function<RegisterCustomerModel, RegisterCustomer> mapRegisterCustomerToModel;
    private final Function<AddressModel, Address> mapAddressModelToDomain;
    private final Function<GenderModel, Gender> mapGenderModelToDomain;

    private final CustomerDisplay customers;
    private final CustomerRegistration registration;
    private final CustomerModification details;

    @Autowired
    CustomerController(final CustomerDisplay customers,
                       final CustomerRegistration registration,
                       final CustomerModification details) {
        this(
            customers,
            registration,
            details,
            CustomerMappingFactory.createCustomerToModelMapper(),
            CustomerMappingFactory.createRegisterCustomerModelToDomainMapper(),
            CustomerMappingFactory.createAddressModelToAddressMapper(),
            CustomerMappingFactory.createGenderModelToDomainMapper()
        );
    }

    CustomerController(final CustomerDisplay customers,
                       final CustomerRegistration registration,
                       final CustomerModification modification,
                       final Function<Customer, CustomerModel> mapCustomerToModel,
                       final Function<RegisterCustomerModel, RegisterCustomer> mapRegisterCustomerToModel,
                       final Function<AddressModel, Address> mapAddressModelToDomain,
                       final Function<GenderModel, Gender> mapGenderModelToDomain) {
        this.customers = customers;
        this.registration = registration;
        this.details = modification;
        this.mapCustomerToModel = mapCustomerToModel;
        this.mapRegisterCustomerToModel = mapRegisterCustomerToModel;
        this.mapAddressModelToDomain = mapAddressModelToDomain;
        this.mapGenderModelToDomain = mapGenderModelToDomain;
    }

    @Override
    public ResponseEntity<CustomerModel> changeDetails(final String customerId,
                                                       final ChangeCustomerDetailsModel model) {
        final ChangeCustomerDetails cmd = assembleCommand(customerId, model);

        final Customer changed = details.change(cmd);
        final CustomerModel response = mapCustomerToModel.apply(changed);

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<List<CustomerModel>> displayAll() {
        final List<CustomerModel> response = customers.displayAll()
            .stream()
            .map(mapCustomerToModel)
            .toList();

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<CustomerModel> displayBy(final String rawCustomerId) {
        return customers.displayBy(CustomerId.from(rawCustomerId))
            .map(mapCustomerToModel)
            .map(ResponseEntity::ok)
            .orElseGet(ResponseEntity.notFound()::build);
    }

    @Override
    public ResponseEntity<CustomerModel> register(final RegisterCustomerModel model) {
        final RegisterCustomer cmd = mapRegisterCustomerToModel.apply(model);
        final Customer registered = registration.register(cmd);

        return ResponseEntity.created(
            URI.create(BASE_PATH + registered.customerId().asText())
        ).body(mapCustomerToModel.apply(registered));
    }

    private ChangeCustomerDetails assembleCommand(final String customerId,
                                                  final ChangeCustomerDetailsModel model) {
        return new ChangeCustomerDetails.Builder()
            .customerId(CustomerId.from(customerId))
            .fullName(FullName.from(model.getFirstName(), model.getLastName()))
            .gender(mapGenderModelToDomain.apply(model.getGender()))
            .birthDate(model.getBirthDate())
            .address(mapAddressModelToDomain.apply(model.getAddress()))
            .email(EmailAddress.from(model.getEmailAddress()))
            .assemble();
    }
}
