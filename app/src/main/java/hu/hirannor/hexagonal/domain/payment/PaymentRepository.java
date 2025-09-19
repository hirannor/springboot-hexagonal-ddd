package hu.hirannor.hexagonal.domain.payment;

import hu.hirannor.hexagonal.domain.order.OrderId;
import java.util.Optional;

public interface PaymentRepository {
    void save(final Payment payment);
    Optional<Payment> findBy(final PaymentId paymentId);
    Optional<Payment> findBy(final OrderId orderId);

}
