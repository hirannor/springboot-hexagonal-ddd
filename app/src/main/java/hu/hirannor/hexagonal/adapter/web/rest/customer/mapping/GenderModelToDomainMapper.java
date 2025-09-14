package hu.hirannor.hexagonal.adapter.web.rest.customer.mapping;

import hu.hirannor.hexagonal.adapter.web.rest.customer.model.GenderModel;
import hu.hirannor.hexagonal.domain.customer.Gender;

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
