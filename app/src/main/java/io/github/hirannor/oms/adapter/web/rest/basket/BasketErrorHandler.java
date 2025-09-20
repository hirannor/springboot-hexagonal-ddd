package io.github.hirannor.oms.adapter.web.rest.basket;

import io.github.hirannor.oms.adapter.web.rest.customer.model.ProblemDetailsModel;
import io.github.hirannor.oms.application.service.basket.error.BasketNotFound;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

@ControllerAdvice(basePackageClasses = BasketController.class)
class BasketErrorHandler {

    BasketErrorHandler() {
    }

    @ExceptionHandler(BasketNotFound.class)
    ResponseEntity<?> basketNotFound(final BasketNotFound ex, final HttpServletRequest request) {
        final ProblemDetailsModel message = new ProblemDetailsModel()
                .timestamp(Instant.now())
                .status(HttpStatus.NOT_FOUND.value())
                .title("Basket not found")
                .instance(request.getRequestURI())
                .detail(ex.getMessage());

        return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
    }
}
