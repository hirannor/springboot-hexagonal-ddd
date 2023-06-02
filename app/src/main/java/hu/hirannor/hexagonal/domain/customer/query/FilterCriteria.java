package hu.hirannor.hexagonal.domain.customer.query;

import hu.hirannor.hexagonal.domain.customer.EmailAddress;
import hu.hirannor.hexagonal.domain.customer.Gender;
import hu.hirannor.hexagonal.infrastructure.query.Query;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

/**
 * Record representation of a filter criteria, which is used to filter the customers
 *
 * @param from   {@link LocalDate} birth date from
 * @param to     {@link LocalDate} birth date from
 * @param gender {@link Gender}
 * @param email  {@link EmailAddress}
 * @author Mate Karolyi
 */
public record FilterCriteria(Optional<LocalDate> from,
                             Optional<LocalDate> to,
                             Optional<Gender> gender,
                             Optional<EmailAddress> email) implements Query {

    /**
     * Retrieves an optional {@link LocalDate} which point to the end of day.
     * The to date handled as exclusive.
     *
     * @return {@link Instant}
     */
    public Optional<LocalDate> toExclusive() {
        return this.to.map(this::addOneDay);
    }

    /**
     * Add one day to the given parameter {@link LocalDate}
     *
     * @param time to be adjusted
     * @return adjusted time
     */
    private LocalDate addOneDay(final LocalDate time) {
        return time.plus(1L, ChronoUnit.DAYS);
    }

    public static class Builder {
        private Optional<LocalDate> from;
        private Optional<LocalDate> to;
        private Optional<Gender> gender;
        private Optional<EmailAddress> email;

        public Builder from(final Optional<LocalDate> from) {
            this.from = from;
            return this;
        }

        public Builder to(final Optional<LocalDate> to) {
            this.to = to;
            return this;
        }

        public Builder gender(final Optional<Gender> gender) {
            this.gender = gender;
            return this;
        }

        public Builder email(final Optional<EmailAddress> email) {
            this.email = email;
            return this;
        }

        public FilterCriteria assemble() {
            return new FilterCriteria(
                from,
                to,
                gender,
                email
            );
        }
    }
}
