package io.github.hirannor.oms.adapter.web.rest.authentication;

import io.github.hirannor.oms.adapter.web.rest.customer.model.ProblemDetailsModel;
import io.github.hirannor.oms.application.service.authentication.error.AuthUserNotFound;
import io.github.hirannor.oms.application.service.authentication.error.InvalidJwtToken;
import io.github.hirannor.oms.application.service.authentication.error.InvalidPassword;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

@ControllerAdvice
class AuthenticationErrorHandler {

    AuthenticationErrorHandler() {
    }

    @ExceptionHandler(InvalidPassword.class)
    ResponseEntity<?> invalidPassword(final InvalidPassword ex, final HttpServletRequest request) {
        final ProblemDetailsModel message = new ProblemDetailsModel()
                .timestamp(Instant.now())
                .status(HttpStatus.UNAUTHORIZED.value())
                .title(HttpStatus.UNAUTHORIZED.getReasonPhrase())
                .instance(request.getRequestURI())
                .detail(ex.getMessage());

        return new ResponseEntity<>(message, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(InvalidJwtToken.class)
    ResponseEntity<?> invalidToken(final InvalidJwtToken ex, final HttpServletRequest request) {
        final ProblemDetailsModel message = new ProblemDetailsModel()
                .timestamp(Instant.now())
                .status(HttpStatus.UNAUTHORIZED.value())
                .title(HttpStatus.UNAUTHORIZED.getReasonPhrase())
                .instance(request.getRequestURI())
                .detail(ex.getMessage());

        return new ResponseEntity<>(message, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AuthUserNotFound.class)
    ResponseEntity<?> userNotFound(final AuthUserNotFound ex, final HttpServletRequest request) {
        final ProblemDetailsModel message = new ProblemDetailsModel()
                .timestamp(Instant.now())
                .status(HttpStatus.UNAUTHORIZED.value())
                .title(HttpStatus.UNAUTHORIZED.getReasonPhrase())
                .instance(request.getRequestURI())
                .detail(ex.getMessage());

        return new ResponseEntity<>(message, HttpStatus.UNAUTHORIZED);
    }
}
