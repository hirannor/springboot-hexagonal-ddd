package hu.hirannor.hexagonal.adapter.persistence.jpa.customer.mapping;

import hu.hirannor.hexagonal.adapter.persistence.jpa.customer.model.CountryModel;
import hu.hirannor.hexagonal.adapter.persistence.jpa.customer.model.CustomerView;
import hu.hirannor.hexagonal.adapter.persistence.jpa.customer.model.GenderModel;
import hu.hirannor.hexagonal.domain.CustomerId;
import hu.hirannor.hexagonal.domain.EmailAddress;
import hu.hirannor.hexagonal.domain.customer.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("CustomerViewToDomainMapper")
class CustomerViewToDomainMapperComponentTest {

    private final Function<CustomerView, Customer> mapper;

    CustomerViewToDomainMapperComponentTest() {
        mapper = new CustomerViewToDomainMapper();
    }

    @Test
    @DisplayName("should map null to literal null")
    void testNull() {
        final Customer mappingResult = mapper.apply(null);
        assertThat(mappingResult).isNull();
    }

    @Test
    @DisplayName("should map customer model to domain")
    void testValidMapping() {
        final CustomerId id = CustomerId.generate();

        final Customer expected = constructExpectedDomain(id);
        final CustomerView input = constructInput(id);

        final Customer result = mapper.apply(input);
        assertThat(result).usingRecursiveComparison().isEqualTo(expected);
    }

    private CustomerView constructInput(final CustomerId id) {
        return new CustomerViewRecord(
                id.asText(),
                "John",
                "Doe",
                LocalDate.of(1992, 2, 10),
                GenderModel.MALE,
                CountryModel.HUNGARY,
                3529,
                "Miskolc",
                "Szentgyörgy str 34",
                "john.doe@test.com"
        );
    }

    private Customer constructExpectedDomain(final CustomerId id) {
        return Customer.from(
                id,
                FullName.from("John", "Doe"),
                LocalDate.of(1992, 2, 10),
                Gender.MALE,
                Address.from(
                        Country.HUNGARY,
                        "Miskolc",
                        PostalCode.from(3529),
                        "Szentgyörgy str 34"
                ),
                EmailAddress.from("john.doe@test.com")
        );
    }
}
