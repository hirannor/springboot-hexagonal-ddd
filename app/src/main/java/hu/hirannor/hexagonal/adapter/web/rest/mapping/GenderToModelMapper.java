package hu.hirannor.hexagonal.adapter.web.rest.mapping;

import hu.hirannor.hexagonal.adapter.web.rest.model.GenderModel;
import hu.hirannor.hexagonal.domain.customer.Gender;
import java.util.function.Function;

/**
 * Maps a {@link Gender} domain type to {@link GenderModel} model type.
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