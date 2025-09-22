package io.github.hirannor.oms.adapter.web.rest.error;

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

    GlobalErrorHandler() {
    }

    @ExceptionHandler(IllegalArgumentException.class)
    ResponseEntity<ProblemDetailsModel> badRequest(
            final IllegalArgumentException ex,
            final HttpServletRequest request
    ) {
        final ProblemDetailsModel message = ProblemDetailsModel.builder()
                .timestamp(Instant.now())
                .title(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .status(HttpStatus.BAD_REQUEST.value())
                .detail(ex.getMessage())
                .instance(request.getRequestURI())
                .build();

        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetailsModel> validationError(
            final MethodArgumentNotValidException ex,
            final HttpServletRequest request
    ) {
        final Map<String, String> errors = new HashMap<>();

        final List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        for (final FieldError error : fieldErrors) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        final ProblemDetailsModel problem = ProblemDetailsModel.builder()
                .timestamp(Instant.now())
                .title("Validation failed")
                .status(HttpStatus.BAD_REQUEST.value())
                .detail("One or more fields are invalid")
                .instance(request.getRequestURI())
                .fields(errors)
                .build();

        return ResponseEntity.badRequest().body(problem);
    }
}
