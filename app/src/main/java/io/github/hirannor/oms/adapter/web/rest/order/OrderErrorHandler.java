package io.github.hirannor.oms.adapter.web.rest.order;

import io.github.hirannor.oms.adapter.web.rest.customer.model.ProblemDetailsModel;
import io.github.hirannor.oms.application.service.order.error.OrderCannotBeCreatedWithoutAddress;
import io.github.hirannor.oms.application.service.order.error.OrderNotFound;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

@ControllerAdvice(basePackageClasses = OrderController.class)
class OrderErrorHandler {

    OrderErrorHandler() {
    }

    @ExceptionHandler(OrderNotFound.class)
    ResponseEntity<?> orderNotFound(final OrderNotFound ex, final HttpServletRequest request) {
        final ProblemDetailsModel message = new ProblemDetailsModel()
                .timestamp(Instant.now())
                .status(HttpStatus.NOT_FOUND.value())
                .title("Order not found")
                .instance(request.getRequestURI())
                .detail(ex.getMessage());

        return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(OrderCannotBeCreatedWithoutAddress.class)
    ResponseEntity<?> missingAddressDetails(final OrderCannotBeCreatedWithoutAddress ex, final HttpServletRequest request) {
        final ProblemDetailsModel message = new ProblemDetailsModel()
                .timestamp(Instant.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .title(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .instance(request.getRequestURI())
                .detail(ex.getMessage());

        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

}
