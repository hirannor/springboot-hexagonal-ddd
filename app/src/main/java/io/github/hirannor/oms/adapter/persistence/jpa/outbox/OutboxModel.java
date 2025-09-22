package io.github.hirannor.oms.adapter.persistence.jpa.outbox;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "EC_OUTBOX_EVENT")
public class OutboxModel {
    private static final int ALLOCATION_SIZE = 5;

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "outbox_seq"
    )
    @SequenceGenerator(
            name = "outbox_seq",
            sequenceName = "outbox_seq",
            allocationSize = ALLOCATION_SIZE
    )
    private Long id;

    @Column(name = "EVENT_ID", nullable = false)
    private String eventId;

    @Column(name = "PROCESSED", nullable = false)
    private boolean processed;

    @Column(name = "EVENT_TYPE", nullable = false)
    private String eventType;

    @Column(name = "CREATED_AT", nullable = false)
    private Instant createdAt;

    @Column(name = "PAYLOAD", nullable = false)
    private String payload;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public boolean isProcessed() {
        return processed;
    }

    public void setProcessed(boolean processed) {
        this.processed = processed;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }
}
