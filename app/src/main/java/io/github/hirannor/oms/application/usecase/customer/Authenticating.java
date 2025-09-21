package io.github.hirannor.oms.application.usecase.customer;

import io.github.hirannor.oms.domain.authentication.AuthenticationResult;
import io.github.hirannor.oms.domain.authentication.AttemptAuthentication;

public interface Authenticating {
    AuthenticationResult authenticate(AttemptAuthentication cmd);
}
