package hu.hirannor.hexagonal.functional;

import hu.hirannor.hexagonal.TestContainerBase;
import hu.hirannor.hexagonal.adapter.web.rest.customer.api.CustomersApi;
import hu.hirannor.hexagonal.adapter.web.rest.customer.model.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DisplayName("CustomerDisplaying")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CustomerDisplayingFunctionalTest extends TestContainerBase {

    private final CustomersApi api;

    @Autowired
    CustomerDisplayingFunctionalTest(final CustomersApi api) {
        this.api = api;
    }

    @Test
    @DisplayName("should display all customer")
    void testDisplayAll() {
        final ResponseEntity<List<CustomerModel>> customers = api.displayAll(
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty()
        );

        assertThat(customers.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(customers.getBody().size()).isEqualTo(3);
    }

    @Test
    @DisplayName("should display customer by id")
    void testDisplayById() {
        final RegisterCustomerModel request = constructRequest();
        final CustomerModel expected = constructExpectedModel();

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

    private CustomerModel constructExpectedModel() {
        return new CustomerModel()
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

    private RegisterCustomerModel constructRequest() {
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
}
