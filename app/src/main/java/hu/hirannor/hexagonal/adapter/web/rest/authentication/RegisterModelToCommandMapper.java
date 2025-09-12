package hu.hirannor.hexagonal.adapter.web.rest.authentication;

import hu.hirannor.hexagonal.adapter.web.rest.authentication.model.RegisterModel;
import hu.hirannor.hexagonal.domain.EmailAddress;
import hu.hirannor.hexagonal.domain.authentication.Password;
import hu.hirannor.hexagonal.domain.authentication.Register;

import java.util.function.Function;

class RegisterModelToCommandMapper implements Function<RegisterModel, Register> {
    @Override
    public Register apply(final RegisterModel model) {
        if (model == null) return null;

        return Register.issue(EmailAddress.from(model.getEmailAddress()), Password.from(model.getPassword()));
    }
}
