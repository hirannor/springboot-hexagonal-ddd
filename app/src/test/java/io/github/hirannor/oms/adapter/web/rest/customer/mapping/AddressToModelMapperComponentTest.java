package io.github.hirannor.oms.adapter.web.rest.customer.mapping;

import io.github.hirannor.oms.adapter.web.rest.customer.model.AddressModel;
import io.github.hirannor.oms.adapter.web.rest.customer.model.CountryModel;
import io.github.hirannor.oms.domain.customer.Address;
import io.github.hirannor.oms.domain.customer.Country;
import io.github.hirannor.oms.domain.customer.PostalCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("AddressToModelMapper")
class AddressToModelMapperComponentTest {

    private final Function<Address, AddressModel> mapper;

    AddressToModelMapperComponentTest() {
        mapper = new AddressToModelMapper();
    }

    @Test
    @DisplayName("should map null to literal null")
    void testNull() {
        final AddressModel mappingResult = mapper.apply(null);
        assertThat(mappingResult).isNull();
    }

    @Test
    @DisplayName("should map emailAddress domain to model emailAddress")
    void testValidMapping() {
        final Address input = constructInput();
        final AddressModel expected = constructExpectedModel();

        final AddressModel result = mapper.apply(input);
        assertThat(result).usingRecursiveComparison().isEqualTo(expected);
    }

    private Address constructInput() {
        return Address.from(
                Country.HUNGARY,
                "Miskolc",
                PostalCode.from(3529),
                "Szentgyörgy str 34"
        );
    }

    private AddressModel constructExpectedModel() {
        return new AddressModel()
                .country(CountryModel.HUNGARY)
                .postalCode(3529)
                .city("Miskolc")
                .streetAddress("Szentgyörgy str 34");
    }
}
