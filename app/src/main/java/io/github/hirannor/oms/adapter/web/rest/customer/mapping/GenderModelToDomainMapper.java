package io.github.hirannor.oms.adapter.web.rest.customer.mapping;

import io.github.hirannor.oms.adapter.web.rest.customer.model.GenderModel;
import io.github.hirannor.oms.domain.customer.Gender;

import java.util.function.Function;

/**
 * Maps a {@link GenderModel} model notificationType to {@link Gender} domain notificationType.
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
