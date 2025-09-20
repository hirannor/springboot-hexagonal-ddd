package io.github.hirannor.oms.adapter.web.rest.customer;

import io.github.hirannor.oms.adapter.web.rest.customer.model.ProblemDetailsModel;
import io.github.hirannor.oms.application.service.customer.error.CustomerNotFound;
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


}
