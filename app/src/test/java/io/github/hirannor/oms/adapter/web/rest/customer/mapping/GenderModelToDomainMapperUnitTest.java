package io.github.hirannor.oms.adapter.web.rest.customer.mapping;

import io.github.hirannor.oms.adapter.web.rest.customer.model.GenderModel;
import io.github.hirannor.oms.domain.customer.Gender;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.function.Function;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("GenderModelToDomainMapper")
class GenderModelToDomainMapperUnitTest {

    private final Function<GenderModel, Gender> mapper;

    GenderModelToDomainMapperUnitTest() {
        mapper = new GenderModelToDomainMapper();
    }

    private static Stream<Arguments> supplyValidCases() {
        return Stream.of(
                Arguments.of(GenderModel.MALE, Gender.MALE),
                Arguments.of(GenderModel.FEMALE, Gender.FEMALE)
        );
    }

    @Test
    @DisplayName("should map null to literal null")
    void testNull() {
        final Gender mappingResult = mapper.apply(null);
        assertThat(mappingResult).isNull();
    }

    @DisplayName("should map")
    @ParameterizedTest(name = "{0} to {1}")
    @MethodSource("supplyValidCases")
    void testValidCases(final GenderModel model, final Gender domain) {
        final Gender mappingResult = mapper.apply(model);

        assertThat(mappingResult).isEqualTo(domain);
    }
}
