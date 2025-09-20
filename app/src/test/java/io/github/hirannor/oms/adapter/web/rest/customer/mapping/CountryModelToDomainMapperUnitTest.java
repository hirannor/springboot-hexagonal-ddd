package io.github.hirannor.oms.adapter.web.rest.customer.mapping;

import io.github.hirannor.oms.adapter.web.rest.customer.model.CountryModel;
import io.github.hirannor.oms.domain.customer.Country;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.function.Function;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("CountryModelToDomainMapper")
class CountryModelToDomainMapperUnitTest {

    private final Function<CountryModel, Country> mapper;

    CountryModelToDomainMapperUnitTest() {
        mapper = new CountryModelToDomainMapper();
    }

    private static Stream<Arguments> supplyValidCases() {
        return Stream.of(
                Arguments.of(CountryModel.GERMANY, Country.GERMANY),
                Arguments.of(CountryModel.HUNGARY, Country.HUNGARY)
        );
    }

    @Test
    @DisplayName("should map null to literal null")
    void testNull() {
        final Country mappingResult = mapper.apply(null);
        assertThat(mappingResult).isNull();
    }

    @DisplayName("should map")
    @ParameterizedTest(name = "{0} to {1}")
    @MethodSource("supplyValidCases")
    void testValidCases(final CountryModel model, final Country domain) {
        final Country mappingResult = mapper.apply(model);

        assertThat(mappingResult).isEqualTo(domain);
    }
}
