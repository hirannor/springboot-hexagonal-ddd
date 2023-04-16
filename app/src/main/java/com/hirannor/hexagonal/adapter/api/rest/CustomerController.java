package com.hirannor.hexagonal.adapter.api.rest;

import com.hirannor.hexagonal.adapter.api.rest.api.CustomersApi;
import com.hirannor.hexagonal.adapter.api.rest.mapping.CustomerMapperFactory;
import com.hirannor.hexagonal.adapter.api.rest.model.CustomerModel;
import com.hirannor.hexagonal.adapter.api.rest.model.SignupRequestModel;
import com.hirannor.hexagonal.application.usecase.customer.CustomerDisplay;
import com.hirannor.hexagonal.application.usecase.customer.CustomerRegistration;
import com.hirannor.hexagonal.domain.customer.AddCustomer;
import com.hirannor.hexagonal.domain.customer.Customer;
import com.hirannor.hexagonal.domain.customer.CustomerId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.function.Function;

@RestController
class CustomerController implements CustomersApi {

    private final Function<Customer, CustomerModel> mapCustomerToModel;
    private final Function<SignupRequestModel, AddCustomer> mapSignupRequestModelToAddCustomer;
    private final CustomerDisplay customers;
    private final CustomerRegistration registration;

    @Autowired
    CustomerController(final CustomerDisplay customers,
                       final CustomerRegistration registration) {
        this(
                customers,
                registration,
                CustomerMapperFactory.createCustomerToModelMapper(),
                CustomerMapperFactory.createSignupRequestModelToAddCustomerMapper()
        );
    }

    CustomerController(final CustomerDisplay customers,
                       final CustomerRegistration registration,
                       final Function<Customer, CustomerModel> mapCustomerToModel,
                       final Function<SignupRequestModel, AddCustomer> mapSignupRequestModelToAddCustomer) {
        this.customers = customers;
        this.registration = registration;
        this.mapCustomerToModel = mapCustomerToModel;
        this.mapSignupRequestModelToAddCustomer = mapSignupRequestModelToAddCustomer;
    }

    @Override
    public ResponseEntity<CustomerModel> displayById(final String rawCustomerId) {
        return customers.displayById(CustomerId.from(rawCustomerId))
                .map(mapCustomerToModel)
                .map(ResponseEntity::ok)
                .orElseGet(ResponseEntity.notFound()::build);
    }

    @Override
    public ResponseEntity<List<CustomerModel>> displayAll() {
        final List<CustomerModel> retrievedCustomers = customers.displayAll()
                .stream()
                .map(mapCustomerToModel)
                .toList();

        return ResponseEntity.ok(retrievedCustomers);
    }

    @Override
    public ResponseEntity<CustomerModel> signup(final SignupRequestModel model) {
        final AddCustomer command = mapSignupRequestModelToAddCustomer.apply(model);

        final Customer registeredCustomer = registration.signup(command);

        final CustomerModel response = mapCustomerToModel.apply(registeredCustomer);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

}
