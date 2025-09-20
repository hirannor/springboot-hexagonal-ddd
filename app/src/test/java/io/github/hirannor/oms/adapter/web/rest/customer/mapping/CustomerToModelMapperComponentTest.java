package io.github.hirannor.oms.adapter.web.rest.customer.mapping;

import io.github.hirannor.oms.adapter.web.rest.customer.model.AddressModel;
import io.github.hirannor.oms.adapter.web.rest.customer.model.CountryModel;
import io.github.hirannor.oms.adapter.web.rest.customer.model.CustomerModel;
import io.github.hirannor.oms.adapter.web.rest.customer.model.GenderModel;
import io.github.hirannor.oms.domain.core.valueobject.CustomerId;
import io.github.hirannor.oms.domain.core.valueobject.EmailAddress;
import io.github.hirannor.oms.domain.customer.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.function.Function;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("CustomerToModelMapper")
class CustomerToModelMapperComponentTest {

    private final Function<Customer, CustomerModel> mapper;

    CustomerToModelMapperComponentTest() {
        mapper = new CustomerToModelMapper();
    }

    @Test
    @DisplayName("should map null to literal null")
    void testNull() {
        final CustomerModel mappingResult = mapper.apply(null);
        assertThat(mappingResult).isNull();
    }

    @Test
    @DisplayName("should map customer domain to model customer")
    void testValidMapping() {
        final CustomerId id = CustomerId.generate();

        final Customer input = constructInput(id);
        final CustomerModel expected = constructExpectedModel(id);

        final CustomerModel result = mapper.apply(input);
        assertThat(result).usingRecursiveComparison().isEqualTo(expected);
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
                FirstName.from("John"),
                LastName.from("Doe"),
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
