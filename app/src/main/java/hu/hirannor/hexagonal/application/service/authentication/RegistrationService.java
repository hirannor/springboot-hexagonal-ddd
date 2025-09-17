package hu.hirannor.hexagonal.application.service.authentication;

import hu.hirannor.hexagonal.application.port.authentication.Authenticator;
import hu.hirannor.hexagonal.application.service.customer.error.CustomerAlreadyExistWithEmailAddress;
import hu.hirannor.hexagonal.application.usecase.customer.Registrating;
import hu.hirannor.hexagonal.domain.authentication.AuthUser;
import hu.hirannor.hexagonal.domain.authentication.Register;
import hu.hirannor.hexagonal.domain.authentication.Role;
import hu.hirannor.hexagonal.domain.core.valueobject.EmailAddress;
import hu.hirannor.hexagonal.domain.customer.Customer;
import hu.hirannor.hexagonal.domain.customer.CustomerRepository;
import hu.hirannor.hexagonal.infrastructure.application.ApplicationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Set;

@ApplicationService
class RegistrationService implements Registrating {

    private static final Logger LOGGER = LogManager.getLogger(
        RegistrationService.class
    );

    private final Authenticator authenticator;
    private final CustomerRepository customers;

    RegistrationService(final Authenticator authenticator, final CustomerRepository customers) {
        this.authenticator = authenticator;
        this.customers = customers;
    }

    @Override
    public void register(final Register command) {
        if (command == null) throw new IllegalArgumentException("Register command cannot be null!");

        LOGGER.info("Registering customer with email: {}", command.emailAddress().asText());

        customers.findByEmailAddress(command.emailAddress())
                .ifPresent(customer -> failBecauseCustomerAlreadyExistBy(customer.emailAddress()));

        final Customer newlyRegistered = Customer.registerBy(command);
        customers.save(newlyRegistered);
        authenticator.register(AuthUser.of(command.emailAddress(), command.password(), Set.of(Role.CUSTOMER)));

        LOGGER.info("Customer with id: {} is successfully registered!", newlyRegistered.id().asText());
    }

    private void failBecauseCustomerAlreadyExistBy(final EmailAddress email) {
        throw new CustomerAlreadyExistWithEmailAddress(
                String.format("Customer already exist with the given e-mail emailAddress: %s", email.asText())
        );
    }
}
