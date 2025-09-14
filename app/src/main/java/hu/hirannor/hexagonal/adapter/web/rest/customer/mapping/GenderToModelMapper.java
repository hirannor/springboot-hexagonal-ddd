package hu.hirannor.hexagonal.adapter.web.rest.customer.mapping;

import hu.hirannor.hexagonal.adapter.web.rest.customer.model.GenderModel;
import hu.hirannor.hexagonal.domain.customer.Gender;

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
