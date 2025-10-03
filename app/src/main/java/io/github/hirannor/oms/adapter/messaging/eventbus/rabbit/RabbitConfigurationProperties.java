package io.github.hirannor.oms.adapter.messaging.eventbus.rabbit;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(value = "messaging.rabbit")
public class RabbitConfigurationProperties {
    private String exchange;
    private String queue;
    private String dlq;
    private String dlx;
    private Outbox outbox;
    private Retry retry;

    public RabbitConfigurationProperties() {
        this.outbox = new Outbox();
        this.retry = new Retry();
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(final String exchange) {
        this.exchange = exchange;
    }

    public String getQueue() {
        return queue;
    }

    public void setQueue(final String queue) {
        this.queue = queue;
    }

    public String getDlq() {
        return dlq;
    }

    public void setDlq(final String dlq) {
        this.dlq = dlq;
    }

    public String getDlx() {
        return dlx;
    }

    public void setDlx(String dlx) {
        this.dlx = dlx;
    }

    public Outbox getOutbox() {
        return outbox;
    }

    public void setOutbox(final Outbox outbox) {
        this.outbox = outbox;
    }

    public Retry getRetry() {
        return retry;
    }

    public void setRetry(Retry retry) {
        this.retry = retry;
    }

    public static class Outbox {
        private Duration pollInterval;
        private int batchSize;

        public int getBatchSize() {
            return batchSize;
        }

        public void setBatchSize(int batchSize) {
            this.batchSize = batchSize;
        }

        public Duration getPollInterval() {
            return pollInterval;
        }

        public void setPollInterval(final Duration pollInterval) {
            this.pollInterval = pollInterval;
        }
    }

    public static class Retry {
        private int maxAttempts;

        public int getMaxAttempts() {
            return maxAttempts;
        }

        public void setMaxAttempts(int maxAttempts) {
            this.maxAttempts = maxAttempts;
        }
    }
}
