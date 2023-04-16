package com.hirannor.hexagonal.adapter.api.rest.mapping;

import com.hirannor.hexagonal.adapter.api.rest.model.GenderModel;
import com.hirannor.hexagonal.domain.customer.Gender;

import java.util.function.Function;

class GenderToModelMapper implements Function<Gender, GenderModel> {

    GenderToModelMapper() {
    }

    @Override
    public GenderModel apply(final Gender domain) {
        if (domain == null) return null;

        return switch (domain) {
            case MALE -> GenderModel.MALE;
            case FEMALE -> GenderModel.FEMALE;
        };
    }

}
