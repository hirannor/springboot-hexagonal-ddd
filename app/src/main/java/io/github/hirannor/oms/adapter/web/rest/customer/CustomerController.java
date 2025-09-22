package io.github.hirannor.oms.adapter.web.rest.customer;

import io.github.hirannor.oms.adapter.web.rest.customer.api.CustomersApi;
import io.github.hirannor.oms.adapter.web.rest.customer.mapping.CustomerMappingFactory;
import io.github.hirannor.oms.adapter.web.rest.customer.model.ChangePersonalDetailsModel;
import io.github.hirannor.oms.adapter.web.rest.customer.model.CustomerModel;
import io.github.hirannor.oms.adapter.web.rest.customer.model.GenderModel;
import io.github.hirannor.oms.application.usecase.authentication.CustomerDeletion;
import io.github.hirannor.oms.application.usecase.customer.CustomerDisplaying;
import io.github.hirannor.oms.application.usecase.customer.CustomerModification;
import io.github.hirannor.oms.domain.core.valueobject.CustomerId;
import io.github.hirannor.oms.domain.core.valueobject.EmailAddress;
import io.github.hirannor.oms.domain.customer.Customer;
import io.github.hirannor.oms.domain.customer.Gender;
import io.github.hirannor.oms.domain.customer.command.ChangePersonalDetails;
import io.github.hirannor.oms.domain.customer.query.FilterCriteria;
import io.github.hirannor.oms.infrastructure.adapter.DriverAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
@RequestMapping(value = "/api")
@DriverAdapter
@Validated
class CustomerController implements CustomersApi {

    private final Function<Customer, CustomerModel> mapCustomerToModel;
    private final Function<GenderModel, Gender> mapGenderModelToDomain;

    private final CustomerDisplaying customers;
    private final CustomerModification personalDetails;
    private final CustomerDeletion customer;

    @Autowired
    CustomerController(final CustomerDisplaying customers,
                       final CustomerModification personalDetails,
                       final CustomerDeletion customer) {
        this(
                customers,
                personalDetails,
                customer,
                CustomerMappingFactory.createCustomerToModelMapper(),
                CustomerMappingFactory.createGenderModelToDomainMapper()
        );
    }

    CustomerController(final CustomerDisplaying customers,
                       final CustomerModification personalDetails,
                       final CustomerDeletion customer,
                       final Function<Customer, CustomerModel> mapCustomerToModel,
                       final Function<GenderModel, Gender> mapGenderModelToDomain) {
        this.customers = customers;
        this.personalDetails = personalDetails;
        this.customer = customer;
        this.mapCustomerToModel = mapCustomerToModel;
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
    public ResponseEntity<CustomerModel> authenticatedCustomer() {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        return customers.displayBy(EmailAddress.from(auth.getName()))
                .map(mapCustomerToModel)
                .map(ResponseEntity::ok)
                .orElseGet(ResponseEntity.notFound()::build);
    }

}
