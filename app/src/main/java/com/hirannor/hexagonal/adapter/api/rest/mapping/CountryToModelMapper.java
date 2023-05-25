package com.hirannor.hexagonal.adapter.api.rest.mapping;

import com.hirannor.hexagonal.adapter.api.rest.model.CountryModel;
import com.hirannor.hexagonal.domain.customer.Country;
import java.util.function.Function;

/**
 * Maps a {@link Country} domain type to {@link CountryModel} model type.
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
