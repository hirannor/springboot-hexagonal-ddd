package hu.hirannor.hexagonal.adapter.web.rest.customer.mapping;

import hu.hirannor.hexagonal.adapter.web.rest.model.CountryModel;
import hu.hirannor.hexagonal.domain.customer.Country;
import java.util.function.Function;

/**
 * Maps a {@link Country} domain type to {@link CountryModel} model type.
 *
 * @author Mate Karolyi
 */
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
