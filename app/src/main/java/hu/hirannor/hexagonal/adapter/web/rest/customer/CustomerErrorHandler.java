package hu.hirannor.hexagonal.adapter.web.rest.customer;

import hu.hirannor.hexagonal.adapter.web.rest.customer.model.ProblemDetailsModel;
import hu.hirannor.hexagonal.application.service.customer.error.CustomerAlreadyExistWithEmailAddress;
import hu.hirannor.hexagonal.application.service.customer.error.CustomerNotFound;
import hu.hirannor.hexagonal.application.service.authentication.error.InvalidPassword;
import jakarta.servlet.http.HttpServletRequest;
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
@ControllerAdvice(basePackageClasses = CustomerController.class)
class CustomerErrorHandler {

    CustomerErrorHandler() {
    }

    @ExceptionHandler(CustomerNotFound.class)
    ResponseEntity<?> customerNotFound(final CustomerNotFound ex, final HttpServletRequest request) {
        final ProblemDetailsModel message = new ProblemDetailsModel()
                .timestamp(Instant.now())
                .status(HttpStatus.NOT_FOUND.value())
                .title("Customer not found")
                .instance(request.getRequestURI())
                .detail(ex.getMessage());

        return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CustomerAlreadyExistWithEmailAddress.class)
    ResponseEntity<?> customerAlreadyExist(final CustomerAlreadyExistWithEmailAddress ex, final HttpServletRequest request) {
        final ProblemDetailsModel message = new ProblemDetailsModel()
                .timestamp(Instant.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .title(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .instance(request.getRequestURI())
                .detail(ex.getMessage());

        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidPassword.class)
    ResponseEntity<?> invalidPassword(final InvalidPassword ex, final HttpServletRequest request) {
        final ProblemDetailsModel message = new ProblemDetailsModel()
                .timestamp(Instant.now())
                .status(HttpStatus.FORBIDDEN.value())
                .title(HttpStatus.FORBIDDEN.getReasonPhrase())
                .instance(request.getRequestURI())
                .detail(ex.getMessage());

        return new ResponseEntity<>(message, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(IllegalStateException.class)
    ResponseEntity<?> badRequest(final IllegalStateException ex, final HttpServletRequest request) {
        final ProblemDetailsModel message = new ProblemDetailsModel()
                .timestamp(Instant.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .title(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .instance(request.getRequestURI())
                .detail(ex.getMessage());

        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

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
}
