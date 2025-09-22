package io.github.hirannor.oms.adapter.web.rest.authentication.mapping;

import io.github.hirannor.oms.adapter.web.rest.authentication.model.RegisterModel;
import io.github.hirannor.oms.domain.authentication.Password;
import io.github.hirannor.oms.domain.authentication.Register;
import io.github.hirannor.oms.domain.core.valueobject.EmailAddress;

import java.util.function.Function;

public class RegisterModelToCommandMapper implements Function<RegisterModel, Register> {

    public RegisterModelToCommandMapper() {
    }

    @Override
    public Register apply(final RegisterModel model) {
        if (model == null) return null;

        return Register.issue(EmailAddress.from(model.getEmailAddress()), Password.from(model.getPassword()));
    }
}
