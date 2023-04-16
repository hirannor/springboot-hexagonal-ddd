package com.hirannor.hexagonal.adapter.api.rest.mapping;

import com.hirannor.hexagonal.adapter.api.rest.model.CountryModel;
import com.hirannor.hexagonal.domain.customer.Country;

import java.util.function.Function;

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
