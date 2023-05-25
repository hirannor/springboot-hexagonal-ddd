package com.hirannor.hexagonal.adapter.persistence.jpa.customer.mapping;

import com.hirannor.hexagonal.adapter.persistence.jpa.customer.model.CountryModel;
import com.hirannor.hexagonal.domain.customer.Country;
import java.util.function.Function;

/**
 * Maps a {@link CountryModel} model type to {@link Country} domain type.
 *
 * @author Mate Karolyi
 */
class CountryModelToDomainMapper implements Function<CountryModel, Country> {

    CountryModelToDomainMapper() {
    }

    @Override
    public Country apply(final CountryModel model) {
        if (model == null) return null;

        return switch (model) {
            case HUNGARY -> Country.HUNGARY;
            case GERMANY -> Country.GERMANY;
        };
    }

}
