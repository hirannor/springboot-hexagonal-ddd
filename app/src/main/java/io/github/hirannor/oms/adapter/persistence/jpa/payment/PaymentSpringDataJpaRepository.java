package io.github.hirannor.oms.adapter.persistence.jpa.payment;

import org.springframework.data.repository.Repository;

import java.util.Optional;

interface PaymentSpringDataJpaRepository extends Repository<PaymentModel, Long> {
    PaymentModel save(PaymentModel order);

    Optional<PaymentModel> findByOrderId(String orderId);

    Optional<PaymentModel> findByPaymentId(String paymentId);

    Optional<PaymentModel> findByProviderReference(String providerReference);

}
