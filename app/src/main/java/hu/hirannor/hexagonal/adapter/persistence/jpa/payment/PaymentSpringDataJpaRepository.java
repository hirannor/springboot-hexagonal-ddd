package hu.hirannor.hexagonal.adapter.persistence.jpa.payment;

import java.util.Optional;
import org.springframework.data.repository.Repository;

interface PaymentSpringDataJpaRepository extends Repository<PaymentModel, Long> {
    PaymentModel save(PaymentModel order);

    Optional<PaymentModel> findByOrderId(String orderId);

    Optional<PaymentModel> findByPaymentId(String paymentId);

    Optional<PaymentModel> findByProviderReference(String providerReference);

}
