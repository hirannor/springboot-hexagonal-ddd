package hu.hirannor.hexagonal.adapter.persistence.jpa.payment;

import hu.hirannor.hexagonal.adapter.persistence.jpa.payment.mapping.PaymentModelToDomainMapper;
import hu.hirannor.hexagonal.adapter.persistence.jpa.payment.mapping.PaymentModeller;
import hu.hirannor.hexagonal.domain.order.OrderId;
import hu.hirannor.hexagonal.domain.payment.Payment;
import hu.hirannor.hexagonal.domain.payment.PaymentId;
import hu.hirannor.hexagonal.domain.payment.PaymentRepository;
import hu.hirannor.hexagonal.infrastructure.adapter.DrivenAdapter;
import hu.hirannor.hexagonal.infrastructure.event.EventPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.function.Function;

@Repository
@Transactional(
    propagation = Propagation.MANDATORY,
    isolation = Isolation.REPEATABLE_READ
)
@DrivenAdapter
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
    @EventPublisher
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
