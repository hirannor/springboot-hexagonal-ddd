package io.github.hirannor.oms.domain.customer.query;

import io.github.hirannor.oms.domain.core.valueobject.EmailAddress;
import io.github.hirannor.oms.domain.customer.Gender;
import io.github.hirannor.oms.infrastructure.query.Query;

import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

/**
 * Record representation of a filter criteria, which is used to filter the customers
 *
 * @param birthDateFrom {@link LocalDate} birth date from
 * @param birthDateTo   {@link LocalDate} birth date from
 * @param gender        {@link Gender}
 * @param email         {@link EmailAddress}
 * @author Mate Karolyi
 */
public record FilterCriteria(Optional<LocalDate> birthDateFrom,
                             Optional<LocalDate> birthDateTo,
                             Optional<Gender> gender,
                             Optional<EmailAddress> email) implements Query {

    /**
     * Retrieves an optional {@link LocalDate} which point to the end of day.
     * The to date handled as exclusive.
     *
     * @return {@link Instant}
     */
    public Optional<LocalDate> birthDateToToExclusive() {
        return this.birthDateTo.map(this::addOneDay);
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
        private Optional<LocalDate> birthDateFrom;
        private Optional<LocalDate> birthDateTo;
        private Optional<Gender> gender;
        private Optional<EmailAddress> email;

        public Builder birthDateFrom(final Optional<LocalDate> birthDateFrom) {
            this.birthDateFrom = birthDateFrom;
            return this;
        }

        public Builder birthDateTo(final Optional<LocalDate> birthDateTo) {
            this.birthDateTo = birthDateTo;
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
                    birthDateFrom,
                    birthDateTo,
                    gender,
                    email
            );
        }
    }
}
