package com.hirannor.hexagonal.adapter.persistence.jpa.customer.mapping;

import com.hirannor.hexagonal.adapter.persistence.jpa.customer.model.GenderModel;
import com.hirannor.hexagonal.domain.customer.Gender;
import java.util.function.Function;

/**
 * Maps a {@link GenderModel} model type to {@link Gender} domain type.
 *
 * @author Mate Karolyi
 */
class GenderModelToDomainMapper implements Function<GenderModel, Gender> {

    GenderModelToDomainMapper() {
    }

    @Override
    public Gender apply(final GenderModel model) {
        if (model == null) return null;

        return switch (model) {
            case MALE -> Gender.MALE;
            case FEMALE -> Gender.FEMALE;
        };
    }

}
