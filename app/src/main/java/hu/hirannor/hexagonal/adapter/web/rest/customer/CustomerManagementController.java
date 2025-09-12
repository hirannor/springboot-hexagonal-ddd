package hu.hirannor.hexagonal.adapter.web.rest.customer;

import hu.hirannor.hexagonal.adapter.web.rest.customer.api.CustomersApi;
import hu.hirannor.hexagonal.adapter.web.rest.customer.mapping.CustomerMappingFactory;
import hu.hirannor.hexagonal.adapter.web.rest.customer.model.*;
import hu.hirannor.hexagonal.application.usecase.*;
import hu.hirannor.hexagonal.domain.EmailAddress;
import hu.hirannor.hexagonal.domain.customer.*;
import hu.hirannor.hexagonal.domain.customer.command.ChangePersonalDetails;
import hu.hirannor.hexagonal.domain.customer.command.EnrollCustomer;
import hu.hirannor.hexagonal.domain.customer.query.FilterCriteria;
import hu.hirannor.hexagonal.infrastructure.adapter.DriverAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * Rest controller implementation of {@link CustomersApi}
 *
 * @author Mate Karolyi
 */
@RestController
@DriverAdapter
class CustomerManagementController implements CustomersApi {

    private final Function<Customer, CustomerModel> mapCustomerToModel;
    private final Function<RegisterCustomerModel, EnrollCustomer> mapRegisterCustomerToModel;
    private final Function<GenderModel, Gender> mapGenderModelToDomain;

    private final CustomerDisplaying customers;
    private final CustomerRegistration registration;
    private final CustomerModification personalDetails;
    private final CustomerDeletion customer;

    @Autowired
    CustomerManagementController(final CustomerDisplaying customers,
                                 final CustomerRegistration registration,
                                 final CustomerModification personalDetails,
                                 final CustomerDeletion customer) {
        this(
                customers,
                registration,
                personalDetails,
                customer,
                CustomerMappingFactory.createCustomerToModelMapper(),
                CustomerMappingFactory.createRegisterCustomerModelToDomainMapper(),
                CustomerMappingFactory.createGenderModelToDomainMapper()
        );
    }

    CustomerManagementController(final CustomerDisplaying customers,
                                 final CustomerRegistration registration,
                                 final CustomerModification personalDetails,
                                 final CustomerDeletion customer,
                                 final Function<Customer, CustomerModel> mapCustomerToModel,
                                 final Function<RegisterCustomerModel, EnrollCustomer> mapRegisterCustomerToModel,
                                 final Function<GenderModel, Gender> mapGenderModelToDomain) {
        this.customers = customers;
        this.personalDetails = personalDetails;
        this.customer = customer;
        this.registration = registration;
        this.mapCustomerToModel = mapCustomerToModel;
        this.mapRegisterCustomerToModel = mapRegisterCustomerToModel;
        this.mapGenderModelToDomain = mapGenderModelToDomain;
    }

    @Override
    public ResponseEntity<CustomerModel> changePersonalDetails(final String customerId,
                                                       final ChangePersonalDetailsModel model) {
        final ChangePersonalDetails cmd = CustomerMappingFactory
                .createChangePersonalDetailsModelToDomainMapper(customerId)
                .apply(model);

        final Customer personalDetailsChanged = personalDetails.changePersonalDetailsBy(cmd);

        return ResponseEntity.ok(mapCustomerToModel.apply(personalDetailsChanged));
    }

    @Override
    public ResponseEntity<Void> deleteBy(final String customerId) {
        customer.deleteBy(CustomerId.from(customerId));

        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<CustomerModel>> displayAll(final Optional<LocalDate> birthDateFrom,
                                                          final Optional<LocalDate> birthDateTo,
                                                          final Optional<GenderModel> gender,
                                                          final Optional<String> emailAddress) {

        final FilterCriteria criteria = new FilterCriteria.Builder()
                .birthDateFrom(birthDateFrom)
                .birthDateTo(birthDateTo)
                .gender(gender.map(mapGenderModelToDomain))
                .email(emailAddress.map(EmailAddress::from))
                .assemble();

        final List<CustomerModel> customers = this.customers.displayAllBy(criteria)
                .stream()
                .map(mapCustomerToModel)
                .toList();

        return ResponseEntity.ok(customers);
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
        final EnrollCustomer cmd = mapRegisterCustomerToModel.apply(model);
        final Customer registeredCustomer = registration.enroll(cmd);

        return ResponseEntity
                .created(buildCustomerLocation(registeredCustomer.customerId()))
                .body(mapCustomerToModel.apply(registeredCustomer));
    }

    private URI buildCustomerLocation(final CustomerId id) {
        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id.asText())
                .toUri();
    }

}
