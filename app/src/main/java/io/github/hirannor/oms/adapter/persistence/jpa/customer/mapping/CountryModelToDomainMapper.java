package io.github.hirannor.oms.adapter.persistence.jpa.customer.mapping;

import io.github.hirannor.oms.adapter.persistence.jpa.customer.model.CountryModel;
import io.github.hirannor.oms.domain.customer.Country;

import java.util.function.Function;

/**
 * Maps a {@link CountryModel} model notificationType to {@link Country} domain notificationType.
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
