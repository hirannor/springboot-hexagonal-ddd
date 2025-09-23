package io.github.hirannor.oms.adapter.web.rest.product;

import io.github.hirannor.oms.adapter.web.rest.products.model.ProblemDetailsModel;
import io.github.hirannor.oms.application.service.product.ProductNotFound;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

@ControllerAdvice
class ProductErrorHandler {

    ProductErrorHandler() {
    }

    @ExceptionHandler(ProductNotFound.class)
    ResponseEntity<ProblemDetailsModel> productNotFound(final ProductNotFound ex, final HttpServletRequest request) {
        final ProblemDetailsModel message = new ProblemDetailsModel()
                .timestamp(Instant.now())
                .status(HttpStatus.NOT_FOUND.value())
                .title("Product not found")
                .instance(request.getRequestURI())
                .detail(ex.getMessage());

        return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
    }

}
