package hu.hirannor.hexagonal.adapter.persistence.jpa.payment;

import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(
        propagation = Propagation.MANDATORY,
        isolation = Isolation.REPEATABLE_READ
)
interface PaymentSpringDataJpaRepository extends Repository<PaymentModel, Long> {
    PaymentModel save(PaymentModel order);

    Optional<PaymentModel> findByOrderId(String orderId);

    Optional<PaymentModel> findByPaymentId(String paymentId);

    Optional<PaymentModel> findByProviderReference(String providerReference);

}
