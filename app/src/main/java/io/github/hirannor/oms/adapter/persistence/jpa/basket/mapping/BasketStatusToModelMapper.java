package io.github.hirannor.oms.adapter.persistence.jpa.basket.mapping;

import io.github.hirannor.oms.adapter.persistence.jpa.basket.BasketStatusModel;
import io.github.hirannor.oms.domain.basket.BasketStatus;

import java.util.function.Function;

public class BasketStatusToModelMapper implements Function<BasketStatus, BasketStatusModel> {
    public BasketStatusToModelMapper() {
    }

    @Override
    public BasketStatusModel apply(final BasketStatus domain) {
        if (domain == null) return null;

        return switch (domain) {
            case CHECKED_OUT -> BasketStatusModel.CHECKED_OUT;
            case ACTIVE -> BasketStatusModel.ACTIVE;
            case EXPIRED -> BasketStatusModel.EXPIRED;
        };
    }
}
