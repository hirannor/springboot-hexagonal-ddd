package io.github.hirannor.oms.domain.payment;

import io.github.hirannor.oms.domain.order.OrderId;

import java.util.Optional;

public interface PaymentRepository {
    void save(final Payment payment);

    Optional<Payment> findBy(final PaymentId paymentId);

    Optional<Payment> findBy(final OrderId orderId);

}
