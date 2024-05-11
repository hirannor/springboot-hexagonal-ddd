package hu.hirannor.hexagonal.adapter.web.rest.customer.mapping;

import hu.hirannor.hexagonal.adapter.web.rest.customer.model.*;
import hu.hirannor.hexagonal.domain.customer.*;
import hu.hirannor.hexagonal.domain.customer.command.EnrollCustomer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("RegisterCustomerModelToDomainMapper")
class EnrollCustomerModelToDomainMapperComponentTest {

    private final Function<RegisterCustomerModel, EnrollCustomer> mapper;

    EnrollCustomerModelToDomainMapperComponentTest() {
        mapper = new RegisterCustomerModelToDomainMapper();
    }

    @Test
    @DisplayName("should map null to literal null")
    void testNull() {
        final EnrollCustomer mappingResult = mapper.apply(null);
        assertThat(mappingResult).isNull();
    }

    @Test
    @DisplayName("should map customer domain to model customer")
    void testValidMapping() {
        final RegisterCustomerModel input = constructInput();

        final EnrollCustomer expected = constructExpectedResult();

        final EnrollCustomer result = mapper.apply(input);

        assertThat(result).usingRecursiveComparison()
                .ignoringFields("id", "registeredAt")
                .isEqualTo(expected);
    }

    private EnrollCustomer constructExpectedResult() {
        return EnrollCustomer.issue(
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

    private RegisterCustomerModel constructInput() {
        return new RegisterCustomerModel()
                .firstName("John")
                .lastName("Doe")
                .gender(GenderModel.MALE)
                .birthDate(LocalDate.of(1992, 2, 10))
                .address(
                        new AddressModel()
                                .country(CountryModel.HUNGARY)
                                .postalCode(3529)
                                .city("Miskolc")
                                .streetAddress("Szentgyörgy str 34")
                )
                .emailAddress("john.doe@test.com");
    }

    private CustomerModel constructExpectedModel(final CustomerId id) {
        return new CustomerModel()
                .customerId(id.asText())
                .firstName("John")
                .lastName("Doe")
                .gender(GenderModel.MALE)
                .birthDate(LocalDate.of(1992, 2, 10))
                .address(new AddressModel()
                        .country(CountryModel.HUNGARY)
                        .postalCode(3529)
                        .city("Miskolc")
                        .streetAddress("Szentgyörgy str 34"))
                .emailAddress("john.doe@test.com");
    }

    private Customer constructInput(final CustomerId id) {
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
