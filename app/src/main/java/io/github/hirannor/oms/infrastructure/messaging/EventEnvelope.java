package io.github.hirannor.oms.infrastructure.messaging;


public record EventEnvelope(String type, String payload) {
}
