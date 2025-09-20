package io.github.hirannor.oms.adapter.web.rest.customer.mapping;

import io.github.hirannor.oms.adapter.web.rest.customer.model.CountryModel;
import io.github.hirannor.oms.domain.customer.Country;

import java.util.function.Function;

/**
 * Maps a {@link Country} domain notificationType to {@link CountryModel} model notificationType.
 *
 * @author Mate Karolyi
 */
class CountryToModelMapper implements Function<Country, CountryModel> {

    CountryToModelMapper() {
    }

    @Override
    public CountryModel apply(final Country domain) {
        if (domain == null) return null;

        return switch (domain) {
            case HUNGARY -> CountryModel.HUNGARY;
            case GERMANY -> CountryModel.GERMANY;
        };
    }

}
