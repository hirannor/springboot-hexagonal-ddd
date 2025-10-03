package io.github.hirannor.oms.application.service.housekeeping.outbox;

import io.github.hirannor.oms.application.port.outbox.Outbox;
import io.github.hirannor.oms.infrastructure.application.ApplicationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@ApplicationService
class OutboxHouseKeepingService {

    private static final Logger LOGGER = LogManager.getLogger(
            OutboxHouseKeepingService.class
    );

    private final Outbox outbox;

    @Autowired
    OutboxHouseKeepingService(final Outbox outbox) {
        this.outbox = outbox;
    }

    @Scheduled(cron = "${outbox.house-keeping-cron}")
    void cleanup() {
        LOGGER.info("Starting outbox housekeeping");

        outbox.deletePublishedOlderThan(Instant.now().minus(7, ChronoUnit.DAYS));

        LOGGER.info("Outbox housekeeping completed");
    }
}