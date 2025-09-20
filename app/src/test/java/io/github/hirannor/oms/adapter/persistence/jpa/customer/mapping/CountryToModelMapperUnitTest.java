package io.github.hirannor.oms.adapter.persistence.jpa.customer.mapping;

import io.github.hirannor.oms.adapter.persistence.jpa.customer.model.CountryModel;
import io.github.hirannor.oms.domain.customer.Country;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.function.Function;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("CountryToModelMapper")
class CountryToModelMapperUnitTest {

    private final Function<Country, CountryModel> mapper;

    CountryToModelMapperUnitTest() {
        mapper = new CountryToModelMapper();
    }

    private static Stream<Arguments> supplyValidCases() {
        return Stream.of(
                Arguments.of(Country.HUNGARY, CountryModel.HUNGARY),
                Arguments.of(Country.GERMANY, CountryModel.GERMANY)
        );
    }

    @Test
    @DisplayName("should map null to literal null")
    void testNull() {
        final CountryModel mappingResult = mapper.apply(null);
        assertThat(mappingResult).isNull();
    }

    @DisplayName("should map")
    @ParameterizedTest(name = "{0} to {1}")
    @MethodSource("supplyValidCases")
    void testValidCases(final Country domain, final CountryModel model) {
        final CountryModel mappingResult = mapper.apply(domain);

        assertThat(mappingResult).isEqualTo(model);
    }
}
