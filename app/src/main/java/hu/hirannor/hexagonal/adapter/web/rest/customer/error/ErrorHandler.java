package hu.hirannor.hexagonal.adapter.web.rest.customer.error;

import hu.hirannor.hexagonal.adapter.web.rest.customer.model.ErrorMessageModel;
import hu.hirannor.hexagonal.application.error.CustomerAlreadyExistWithEmailAddress;
import hu.hirannor.hexagonal.application.error.CustomerNotFound;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

/**
 * Controller advice implementation to handle different errors.
 *
 * @author Mate Karolyi
 */
@ControllerAdvice
class ErrorHandler {

    ErrorHandler() {
    }

    @ExceptionHandler(CustomerNotFound.class)
    ResponseEntity<?> customerNotFound(final CustomerNotFound ex) {
        final ErrorMessageModel message = new ErrorMessageModel()
                .timestamp(Instant.now())
                .status(HttpStatus.NOT_FOUND.toString())
                .message(ex.getMessage());

        return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CustomerAlreadyExistWithEmailAddress.class)
    ResponseEntity<?> customerAlreadyExist(final CustomerAlreadyExistWithEmailAddress ex) {
        final ErrorMessageModel message = new ErrorMessageModel()
                .timestamp(Instant.now())
                .status(HttpStatus.BAD_REQUEST.toString())
                .message(ex.getMessage());

        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    ResponseEntity<?> internalServerError(final IllegalArgumentException ex) {
        final ErrorMessageModel message = new ErrorMessageModel()
                .timestamp(Instant.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                .message(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());

        return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
