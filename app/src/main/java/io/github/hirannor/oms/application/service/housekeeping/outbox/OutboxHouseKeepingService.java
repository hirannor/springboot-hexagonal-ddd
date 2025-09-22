package io.github.hirannor.oms.application.service.housekeeping.outbox;

import io.github.hirannor.oms.application.port.outbox.Outbox;
import io.github.hirannor.oms.infrastructure.application.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@ApplicationService
public class OutboxHouseKeepingService {
    private final Outbox outbox;

    @Autowired
    public OutboxHouseKeepingService(final Outbox outbox) {
        this.outbox = outbox;
    }

    @Scheduled(cron = "0 */5 * * * *")
    public void cleanup() {
        outbox.deleteProcessedOlderThan(Instant.now().minus(7, ChronoUnit.DAYS));
    }
}