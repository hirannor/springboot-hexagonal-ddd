package io.github.hirannor.oms.adapter.persistence.inmemory;

import io.github.hirannor.oms.domain.core.valueobject.CustomerId;
import io.github.hirannor.oms.domain.core.valueobject.EmailAddress;
import io.github.hirannor.oms.domain.customer.Customer;
import io.github.hirannor.oms.domain.customer.CustomerRepository;
import io.github.hirannor.oms.domain.customer.query.FilterCriteria;
import io.github.hirannor.oms.infrastructure.adapter.DrivenAdapter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Naive implementation of {@link CustomerRepository}
 *
 * @author Mate Karolyi
 */
@Component
@DrivenAdapter
class CustomerInMemoryRepository implements CustomerRepository {

    private final Map<CustomerId, Customer> customerStore;
    private final Map<EmailAddress, Customer> emailIndex;

    CustomerInMemoryRepository() {
        this.customerStore = new ConcurrentHashMap<>();
        this.emailIndex = new ConcurrentHashMap<>();
    }

    @Override
    public void deleteBy(final CustomerId customerId) {
        final Customer removed = customerStore.remove(customerId);
        if (removed != null)
            emailIndex.remove(removed.emailAddress());
    }

    @Override
    public List<Customer> findAllBy(final FilterCriteria query) {
        return customerStore.values().stream()
                .filter(matches(Customer::birthDate, query.birthDateFrom(), this::notBefore))
                .filter(matches(Customer::birthDate, query.birthDateTo(), this::notAfter))
                .filter(matches(Customer::gender, query.gender(), Objects::equals))
                .filter(matches(Customer::emailAddress, query.email(), Objects::equals))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Customer> findBy(final CustomerId customerId) {
        return Optional.ofNullable(customerStore.get(customerId));
    }

    @Override
    public Optional<Customer> findByEmailAddress(final EmailAddress email) {
        return Optional.ofNullable(emailIndex.get(email));
    }

    @Override
    public void save(final Customer customer) {
        customerStore.put(customer.id(), customer);
        emailIndex.put(customer.emailAddress(), customer);
    }

    private <T> Predicate<Customer> matches(
            final Function<Customer, T> getter,
            final Optional<T> value,
            final BiPredicate<T, T> comparator
    ) {
        return customer -> value.map(v -> comparator.test(getter.apply(customer), v))
                .orElse(true);
    }

    private boolean notBefore(final LocalDate actual, final LocalDate from) {
        return !actual.isBefore(from);
    }

    private boolean notAfter(final LocalDate actual, final LocalDate to) {
        return !actual.isAfter(to);
    }
}
