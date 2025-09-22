package io.github.hirannor.oms.application.service.authentication;

import io.github.hirannor.oms.application.port.authentication.Authenticator;
import io.github.hirannor.oms.application.port.outbox.Outbox;
import io.github.hirannor.oms.application.service.customer.error.CustomerAlreadyExistWithEmailAddress;
import io.github.hirannor.oms.application.usecase.customer.Registrating;
import io.github.hirannor.oms.domain.authentication.AuthUser;
import io.github.hirannor.oms.domain.authentication.Register;
import io.github.hirannor.oms.domain.authentication.Role;
import io.github.hirannor.oms.domain.core.valueobject.EmailAddress;
import io.github.hirannor.oms.domain.customer.Customer;
import io.github.hirannor.oms.domain.customer.CustomerRepository;
import io.github.hirannor.oms.infrastructure.application.ApplicationService;
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
    private final Outbox outboxes;

    RegistrationService(final Authenticator authenticator,
                        final CustomerRepository customers,
                        final Outbox outboxes) {
        this.authenticator = authenticator;
        this.customers = customers;
        this.outboxes = outboxes;
    }

    @Override
    public void register(final Register command) {
        if (command == null) throw new IllegalArgumentException("Register command cannot be null!");

        LOGGER.info("Registering customer with emailAddress={}", command.emailAddress().asText());

        customers.findByEmailAddress(command.emailAddress())
                .ifPresent(customer -> failBecauseCustomerAlreadyExistBy(customer.emailAddress()));

        final Customer newlyRegistered = Customer.registerBy(command);
        customers.save(newlyRegistered);

        newlyRegistered.events()
                .forEach(outboxes::save);

        newlyRegistered.clearEvents();

        authenticator.register(AuthUser.of(command.emailAddress(), command.password(), Set.of(Role.CUSTOMER)));

        LOGGER.info("Customer with customerId={} is successfully registered!", newlyRegistered.id().asText());
    }

    private void failBecauseCustomerAlreadyExistBy(final EmailAddress email) {
        throw new CustomerAlreadyExistWithEmailAddress(
                String.format("Customer already exist with the given e-mail emailAddress: %s", email.asText())
        );
    }
}
