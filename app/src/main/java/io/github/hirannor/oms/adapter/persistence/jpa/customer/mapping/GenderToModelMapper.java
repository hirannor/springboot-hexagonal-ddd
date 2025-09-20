package io.github.hirannor.oms.adapter.persistence.jpa.customer.mapping;

import io.github.hirannor.oms.adapter.persistence.jpa.customer.model.GenderModel;
import io.github.hirannor.oms.domain.customer.Gender;

import java.util.function.Function;

/**
 * Maps a {@link Gender} domain notificationType to {@link GenderModel} model notificationType.
 *
 * @author Mate Karolyi
 */
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
