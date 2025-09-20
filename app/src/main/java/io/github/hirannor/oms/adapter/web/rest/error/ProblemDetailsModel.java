package io.github.hirannor.oms.adapter.web.rest.error;

import java.time.Instant;
import java.util.Map;

public record ProblemDetailsModel(Instant timestamp, String title, int status, String detail, String instance,
                                  Map<String, String> fields) {

    public ProblemDetailsModel withTimestamp(final Instant timestamp) {
        return new ProblemDetailsModel(timestamp, title, status, detail, instance, fields);
    }

    public ProblemDetailsModel withTitle(final String title) {
        return new ProblemDetailsModel(timestamp, title, status, detail, instance, fields);
    }

    public ProblemDetailsModel withStatus(final int status) {
        return new ProblemDetailsModel(timestamp, title, status, detail, instance, fields);
    }

    public ProblemDetailsModel withDetail(final String detail) {
        return new ProblemDetailsModel(timestamp, title, status, detail, instance, fields);
    }

    public ProblemDetailsModel withInstance(final String instance) {
        return new ProblemDetailsModel(timestamp, title, status, detail, instance, fields);
    }

    public ProblemDetailsModel withFields(final Map<String, String> fields) {
        return new ProblemDetailsModel(timestamp, title, status, detail, instance, fields);
    }
}
