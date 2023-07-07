package hu.hirannor.hexagonal.functional;

import hu.hirannor.hexagonal.adapter.web.rest.customer.api.CustomersApi;
import hu.hirannor.hexagonal.adapter.web.rest.customer.model.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@ActiveProfiles("test")
@DisplayName("CustomerEnrolling")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CustomerEnrollingFunctionalTest {

    @Container
    private final PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer<>("postgres:14")
            .withDatabaseName("test-db")
            .withUsername("sa")
            .withPassword("sa");

    private final CustomersApi api;

    @Autowired
    CustomerEnrollingFunctionalTest(final CustomersApi api) {
        this.api = api;
    }

    @Test
    @DisplayName("should register a customer")
    void testRegisterCustomer() {
        final RegisterCustomerModel request = constructRequest();
        final CustomerModel expected = constructExpectedModel();

        final ResponseEntity<CustomerModel> result = api.register(request);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(result.getBody())
                .usingRecursiveComparison()
                .ignoringFields("customerId")
                .isEqualTo(expected);
    }

    private CustomerModel constructExpectedModel() {
        return new CustomerModel()
                .firstName("John")
                .lastName("Rambo")
                .gender(GenderModel.MALE)
                .birthDate(LocalDate.of(1992, 2, 10))
                .address(
                        new AddressModel()
                                .country(CountryModel.HUNGARY)
                                .postalCode(3529)
                                .city("Miskolc")
                                .streetAddress("Szentgyörgy str 34")
                )
                .emailAddress("john.rambo@test.com");
    }

    private RegisterCustomerModel constructRequest() {
        return new RegisterCustomerModel()
                .firstName("John")
                .lastName("Rambo")
                .gender(GenderModel.MALE)
                .birthDate(LocalDate.of(1992, 2, 10))
                .address(
                        new AddressModel()
                                .country(CountryModel.HUNGARY)
                                .postalCode(3529)
                                .city("Miskolc")
                                .streetAddress("Szentgyörgy str 34")
                )
                .emailAddress("john.rambo@test.com");
    }
}
