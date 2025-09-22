package io.github.hirannor.oms.adapter.persistence.jpa.payment;

import io.github.hirannor.oms.adapter.persistence.jpa.payment.mapping.PaymentModelToDomainMapper;
import io.github.hirannor.oms.adapter.persistence.jpa.payment.mapping.PaymentModeller;
import io.github.hirannor.oms.domain.order.OrderId;
import io.github.hirannor.oms.domain.payment.Payment;
import io.github.hirannor.oms.domain.payment.PaymentId;
import io.github.hirannor.oms.domain.payment.PaymentRepository;
import io.github.hirannor.oms.infrastructure.adapter.DrivenAdapter;
import io.github.hirannor.oms.infrastructure.adapter.PersistenceAdapter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;
import java.util.function.Function;

@DrivenAdapter
@PersistenceAdapter
class PaymentJpaRepository implements PaymentRepository {
    private final PaymentSpringDataJpaRepository payments;
    private final Function<PaymentModel, Payment> mapModelToDomain;

    @Autowired
    PaymentJpaRepository(final PaymentSpringDataJpaRepository payments) {
        this(payments, new PaymentModelToDomainMapper());
    }

    PaymentJpaRepository(final PaymentSpringDataJpaRepository payments,
                         final Function<PaymentModel, Payment> mapModelToDomain) {
        this.payments = payments;
        this.mapModelToDomain = mapModelToDomain;
    }

    @Override
    public void save(final Payment payment) {
        if (payment == null) throw new IllegalArgumentException("payment cannot be null");

        final PaymentModel toPersist = payments.findByOrderId(payment.orderId().asText())
                .orElseGet(PaymentModel::new);

        PaymentModeller.applyChangesFrom(payment).to(toPersist);
        payments.save(toPersist);
    }

    @Override
    public Optional<Payment> findBy(final PaymentId id) {
        if (id == null) throw new IllegalArgumentException("id cannot be null");

        return payments.findByPaymentId(id.asText())
                .map(mapModelToDomain);
    }

    @Override
    public Optional<Payment> findBy(final OrderId id) {
        if (id == null) throw new IllegalArgumentException("id cannot be null");

        return payments.findByOrderId(id.asText())
                .map(mapModelToDomain);
    }
}
