package io.github.hirannor.oms.adapter.web.rest.customer;

import io.github.hirannor.oms.adapter.web.rest.error.ProblemDetailsModel;
import io.github.hirannor.oms.application.service.customer.error.CustomerAlreadyExistWithEmailAddress;
import io.github.hirannor.oms.application.service.customer.error.CustomerNotFound;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

@ControllerAdvice
public class CustomerErrorHandler {

    @ExceptionHandler(CustomerAlreadyExistWithEmailAddress.class)
    ResponseEntity<ProblemDetailsModel> customerAlreadyExist(
            final CustomerAlreadyExistWithEmailAddress ex,
            final HttpServletRequest request
    ) {
        final ProblemDetailsModel message = new ProblemDetailsModel(
                Instant.now(),
                HttpStatus.CONFLICT.getReasonPhrase(),
                HttpStatus.CONFLICT.value(),
                ex.getMessage(),
                request.getRequestURI(),
                null
        );

        return new ResponseEntity<>(message, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(CustomerNotFound.class)
    ResponseEntity<ProblemDetailsModel> customerNotFound(
            final CustomerNotFound ex,
            final HttpServletRequest request
    ) {
        final ProblemDetailsModel message = new ProblemDetailsModel(
                Instant.now(),
                "Customer not found",
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                request.getRequestURI(),
                null
        );

        return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
    }
}
