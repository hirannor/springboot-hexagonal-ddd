package io.github.hirannor.oms.adapter.web.rest.error;

import io.github.hirannor.oms.application.service.customer.error.CustomerAlreadyExistWithEmailAddress;
import io.github.hirannor.oms.application.service.customer.error.CustomerNotFound;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
class GlobalErrorHandler {

    GlobalErrorHandler() {}

    @ExceptionHandler(IllegalArgumentException.class)
    ResponseEntity<ProblemDetailsModel> internalServerError(
            final IllegalArgumentException ex,
            final HttpServletRequest request
    ) {
        final ProblemDetailsModel message = new ProblemDetailsModel(
                Instant.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                ex.getMessage(),
                request.getRequestURI(),
                null
        );

        return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }

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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetailsModel> handleValidation(
            final MethodArgumentNotValidException ex,
            final HttpServletRequest request
    ) {
        final Map<String, String> errors = new HashMap<>();

        final List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        for (final FieldError error : fieldErrors) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        final ProblemDetailsModel problem = new ProblemDetailsModel(
                Instant.now(),
                "Validation failed",
                HttpStatus.BAD_REQUEST.value(),
                "One or more fields are invalid",
                request.getRequestURI(),
                errors
        );

        return ResponseEntity.badRequest().body(problem);
    }
}
