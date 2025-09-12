package hu.hirannor.hexagonal.application.usecase;

import hu.hirannor.hexagonal.domain.authentication.Register;

public interface Registrating {
    void register(final Register command);
}
