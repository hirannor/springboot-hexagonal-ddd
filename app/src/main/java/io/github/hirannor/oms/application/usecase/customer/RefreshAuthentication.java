package io.github.hirannor.oms.application.usecase.customer;

import io.github.hirannor.oms.domain.authentication.AuthenticationResult;
import io.github.hirannor.oms.domain.authentication.RefreshToken;

public interface RefreshAuthentication {
    AuthenticationResult refresh(RefreshToken cmd);
}
