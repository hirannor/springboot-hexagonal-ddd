package io.github.hirannor.oms.adapter.persistence.jpa.outbox;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface OutboxSpringDataJpaRepository extends JpaRepository<OutboxModel, String> {
    List<OutboxModel> findTop50ByProcessedFalseOrderByCreatedAtAsc();
    Optional<OutboxModel> findByEventId(String eventId);

    @Modifying
    void deleteByProcessedIsTrueAndCreatedAtBefore(Instant cutoff);
}
