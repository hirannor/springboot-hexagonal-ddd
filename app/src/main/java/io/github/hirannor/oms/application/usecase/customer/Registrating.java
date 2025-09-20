package io.github.hirannor.oms.application.usecase.customer;

import io.github.hirannor.oms.domain.authentication.Register;

public interface Registrating {
    void register(final Register command);
}
