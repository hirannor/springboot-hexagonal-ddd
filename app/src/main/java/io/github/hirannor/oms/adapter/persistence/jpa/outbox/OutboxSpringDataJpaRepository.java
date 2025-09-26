package io.github.hirannor.oms.adapter.persistence.jpa.outbox;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.time.Instant;
import java.util.Optional;

public interface OutboxSpringDataJpaRepository extends JpaRepository<OutboxModel, String> {
    Slice<OutboxModel> findByProcessedFalseOrderByCreatedAtAsc(Pageable pageable);

    Optional<OutboxModel> findByMessageId(String messageId);

    @Modifying
    void deleteByProcessedIsTrueAndCreatedAtBefore(Instant cutoff);
}
