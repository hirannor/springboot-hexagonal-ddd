package hu.hirannor.hexagonal.integration;

import hu.hirannor.hexagonal.adapter.web.rest.customer.api.CustomersApi;
import hu.hirannor.hexagonal.adapter.web.rest.customer.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DisplayName("CustomerDisplaying")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CustomerDisplayingIntegrationTest {

    @Container
    private final PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer<>("postgres:14")
            .withDatabaseName("test-db")
            .withUsername("sa")
            .withPassword("sa");

    private final CustomersApi api;

    @Autowired
    CustomerDisplayingIntegrationTest(final CustomersApi api) {
        this.api = api;
    }

    @BeforeEach
    void setUp() {
        final MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }

    @Test
    @DisplayName("should display all customer that are filtered with birthDateFrom")
    void testDisplayAll() {
        final RegisterCustomerModel req1 = constructRequest("john.doe2@test.com",
                LocalDate.of(1995, 2, 10));
        final RegisterCustomerModel req2 = constructRequest("john.doe3@test.com",
                LocalDate.of(1996, 10, 10));
        api.register(req1);
        api.register(req2);

        final ResponseEntity<List<CustomerModel>> customers = api.displayAll(
                Optional.of(LocalDate.of(1995, 2, 10)),
                Optional.empty(),
                Optional.empty(),
                Optional.empty()
        );

        assertThat(customers.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(customers.getBody().size()).isEqualTo(2);
    }

    @Test
    @DisplayName("should display customer by id")
    void testDisplayById() {
        final RegisterCustomerModel request = constructRequest("john.doe@test.com",
                LocalDate.of(1992, 2, 10));
        final CustomerModel expected = constructExpectedModel("john.doe@test.com",
                LocalDate.of(1992, 2, 10));

        final ResponseEntity<CustomerModel> registeredCustomer = api.register(request);
        final String customerId = registeredCustomer.getBody().getCustomerId();

        final ResponseEntity<CustomerModel> result = api.displayBy(customerId);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(
                result.getBody())
                .usingRecursiveComparison()
                .ignoringFields("customerId")
                .isEqualTo(expected);

    }

    private CustomerModel constructExpectedModel(final String email, final LocalDate birthDate) {
        return new CustomerModel()
                .firstName("John")
                .lastName("Doe")
                .gender(GenderModel.MALE)
                .birthDate(birthDate)
                .address(
                        new AddressModel()
                                .country(CountryModel.HUNGARY)
                                .postalCode(3529)
                                .city("Miskolc")
                                .streetAddress("Szentgyörgy str 34")
                )
                .emailAddress(email);
    }

    private RegisterCustomerModel constructRequest(final String email, final LocalDate birthDate) {
        return new RegisterCustomerModel()
                .firstName("John")
                .lastName("Doe")
                .gender(GenderModel.MALE)
                .birthDate(birthDate)
                .address(
                        new AddressModel()
                                .country(CountryModel.HUNGARY)
                                .postalCode(3529)
                                .city("Miskolc")
                                .streetAddress("Szentgyörgy str 34")
                )
                .emailAddress(email);
    }
}
