package io.github.hirannor.oms.adapter.web.rest.error;

import io.github.hirannor.oms.adapter.web.rest.customer.model.ProblemDetailsModel;
import io.github.hirannor.oms.application.service.customer.error.CustomerAlreadyExistWithEmailAddress;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

@ControllerAdvice
class GlobalErrorHandler {

    GlobalErrorHandler() {}

    @ExceptionHandler(IllegalArgumentException.class)
    ResponseEntity<?> internalServerError(final IllegalArgumentException ex, final HttpServletRequest request) {
        final ProblemDetailsModel message = new ProblemDetailsModel()
                .timestamp(Instant.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .title(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .instance(request.getRequestURI())
                .detail(ex.getMessage());

        return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(CustomerAlreadyExistWithEmailAddress.class)
    ResponseEntity<?> customerAlreadyExist(final CustomerAlreadyExistWithEmailAddress ex, final HttpServletRequest request) {
        final ProblemDetailsModel message = new ProblemDetailsModel()
                .timestamp(Instant.now())
                .status(HttpStatus.CONFLICT.value())
                .title(HttpStatus.CONFLICT.getReasonPhrase())
                .instance(request.getRequestURI())
                .detail(ex.getMessage());

        return new ResponseEntity<>(message, HttpStatus.CONFLICT);
    }
}
