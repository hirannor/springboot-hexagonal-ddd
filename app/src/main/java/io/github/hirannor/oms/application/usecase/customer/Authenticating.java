package io.github.hirannor.oms.application.usecase.customer;

import io.github.hirannor.oms.domain.authentication.AttemptAuthentication;
import io.github.hirannor.oms.domain.authentication.AuthenticationResult;

public interface Authenticating {
    AuthenticationResult authenticate(AttemptAuthentication cmd);
}
