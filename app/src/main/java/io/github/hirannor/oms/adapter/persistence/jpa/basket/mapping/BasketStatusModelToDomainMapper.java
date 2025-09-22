package io.github.hirannor.oms.adapter.persistence.jpa.basket.mapping;


import io.github.hirannor.oms.adapter.persistence.jpa.basket.BasketStatusModel;
import io.github.hirannor.oms.domain.basket.BasketStatus;

import java.util.function.Function;

public class BasketStatusModelToDomainMapper implements Function<BasketStatusModel, BasketStatus> {
    public BasketStatusModelToDomainMapper() {
    }

    @Override
    public BasketStatus apply(final BasketStatusModel model) {
        if (model == null) return null;

        return switch (model) {
            case CHECKED_OUT -> BasketStatus.CHECKED_OUT;
            case ACTIVE -> BasketStatus.ACTIVE;
            case EXPIRED -> BasketStatus.EXPIRED;
        };
    }
}
