package hu.hirannor.hexagonal.application.service;

import hu.hirannor.hexagonal.domain.error.CustomerAlreadyExistWithEmailAddress;
import hu.hirannor.hexagonal.application.port.authentication.Authenticator;
import hu.hirannor.hexagonal.application.usecase.Registrating;
import hu.hirannor.hexagonal.domain.EmailAddress;
import hu.hirannor.hexagonal.domain.authentication.AuthUser;
import hu.hirannor.hexagonal.domain.authentication.Register;
import hu.hirannor.hexagonal.domain.customer.Customer;
import hu.hirannor.hexagonal.domain.customer.CustomerRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(
    propagation = Propagation.REQUIRES_NEW,
    isolation = Isolation.REPEATABLE_READ
)
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

        LOGGER.info("Registrating customer with email: {}", command.emailAddress());

        customers.findByEmailAddress(command.emailAddress())
                .ifPresent(customer -> failBecauseCustomerAlreadyExistBy(customer.emailAddress()));

        final Customer newlyRegistered = Customer.registerBy(command);
        customers.save(newlyRegistered);
        authenticator.register(AuthUser.of(command.emailAddress(), command.password()));

        LOGGER.info("Customer with id: {} is successfully registered!", newlyRegistered.customerId());
    }

    private void failBecauseCustomerAlreadyExistBy(final EmailAddress email) {
        throw new CustomerAlreadyExistWithEmailAddress(
                String.format("Customer already exist with the given e-mail emailAddress: %s", email.value())
        );
    }
}
