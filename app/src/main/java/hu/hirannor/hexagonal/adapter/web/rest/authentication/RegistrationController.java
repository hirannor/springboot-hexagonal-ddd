package hu.hirannor.hexagonal.adapter.web.rest.authentication;

import hu.hirannor.hexagonal.adapter.web.rest.authentication.api.RegisterApi;
import hu.hirannor.hexagonal.adapter.web.rest.authentication.model.RegisterModel;
import hu.hirannor.hexagonal.application.usecase.Registrating;
import hu.hirannor.hexagonal.domain.authentication.Register;
import hu.hirannor.hexagonal.infrastructure.adapter.DriverAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.function.Function;

@RestController
@RequestMapping(value = "/api")
@DriverAdapter
class RegistrationController implements RegisterApi {

    private final Function<RegisterModel, Register> mapRegisterModelToCommand;
    private final Registrating user;

    @Autowired
    RegistrationController(final Registrating user) {
        this(user, new RegisterModelToCommandMapper());
    }

    RegistrationController(final Registrating user,
                           final Function<RegisterModel, Register> mapRegisterModelToCommand) {
        this.user = user;
        this.mapRegisterModelToCommand = mapRegisterModelToCommand;
    }

    @Override
    public ResponseEntity<Void> register(final RegisterModel registerModel) {
        final Register command = mapRegisterModelToCommand.apply(registerModel);
        user.register(command);

        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }
}
