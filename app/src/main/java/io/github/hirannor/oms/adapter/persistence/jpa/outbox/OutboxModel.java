package io.github.hirannor.oms.adapter.persistence.jpa.outbox;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "EC_OUTBOX_MESSAGE")
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

    @Column(name = "MESSAGE_ID", nullable = false)
    private String messageId;

    @Column(name = "MESSAGE_TYPE", nullable = false)
    private String messageType;

    @Column(name = "CREATED_AT", nullable = false)
    private Instant createdAt;

    @Column(name = "PAYLOAD", nullable = false)
    private String payload;

    @Column(name = "STATUS", nullable = false)
    @Enumerated(EnumType.STRING)
    private OutboxStatusModel status = OutboxStatusModel.PENDING;

    @Column(name = "ATTEMPT_COUNT", nullable = false)
    private int attemptCount = 0;

    @Column(name = "LAST_ATTEMPT_AT")
    private Instant lastAttemptAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String eventId) {
        this.messageId = eventId;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String eventType) {
        this.messageType = eventType;
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

    public OutboxStatusModel getStatus() {
        return status;
    }

    public void setStatus(OutboxStatusModel status) {
        this.status = status;
    }

    public int getAttemptCount() {
        return attemptCount;
    }

    public void setAttemptCount(int attemptCount) {
        this.attemptCount = attemptCount;
    }

    public Instant getLastAttemptAt() {
        return lastAttemptAt;
    }

    public void setLastAttemptAt(Instant lastAttemptAt) {
        this.lastAttemptAt = lastAttemptAt;
    }
}
