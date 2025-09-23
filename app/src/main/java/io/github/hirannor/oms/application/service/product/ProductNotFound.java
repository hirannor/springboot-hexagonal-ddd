package io.github.hirannor.oms.application.service.product;

public class ProductNotFound extends RuntimeException {

    public ProductNotFound(final String message) {
        super(message);
    }

}
