package hu.hirannor.hexagonal.adapter.web.rest.authentication.mapping;

import hu.hirannor.hexagonal.adapter.web.rest.authentication.model.RegisterModel;
import hu.hirannor.hexagonal.domain.authentication.Password;
import hu.hirannor.hexagonal.domain.authentication.Register;
import hu.hirannor.hexagonal.domain.core.valueobject.EmailAddress;

import java.util.function.Function;

public class RegisterModelToCommandMapper implements Function<RegisterModel, Register> {

    public RegisterModelToCommandMapper() {}

    @Override
    public Register apply(final RegisterModel model) {
        if (model == null) return null;

        return Register.issue(EmailAddress.from(model.getEmailAddress()), Password.from(model.getPassword()));
    }
}
