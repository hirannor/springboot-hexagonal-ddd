package io.github.hirannor.oms.adapter.web.rest.error;

import java.time.Instant;
import java.util.Map;

public record ProblemDetailsModel(
        Instant timestamp,
        String title,
        int status,
        String detail,
        String instance,
        Map<String, String> fields
) {

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private Instant timestamp;
        private String title;
        private int status;
        private String detail;
        private String instance;
        private Map<String, String> fields;

        private Builder() {
        }

        public Builder timestamp(final Instant timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Builder title(final String title) {
            this.title = title;
            return this;
        }

        public Builder status(final int status) {
            this.status = status;
            return this;
        }

        public Builder detail(final String detail) {
            this.detail = detail;
            return this;
        }

        public Builder instance(final String instance) {
            this.instance = instance;
            return this;
        }

        public Builder fields(final Map<String, String> fields) {
            this.fields = fields;
            return this;
        }

        public ProblemDetailsModel build() {
            return new ProblemDetailsModel(timestamp, title, status, detail, instance, fields);
        }
    }
}
