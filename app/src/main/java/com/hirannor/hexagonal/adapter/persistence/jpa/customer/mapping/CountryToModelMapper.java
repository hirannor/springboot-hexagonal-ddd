package com.hirannor.hexagonal.adapter.persistence.jpa.customer.mapping;

import com.hirannor.hexagonal.adapter.persistence.jpa.customer.model.CountryModel;
import com.hirannor.hexagonal.domain.customer.Country;

import java.util.function.Function;

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