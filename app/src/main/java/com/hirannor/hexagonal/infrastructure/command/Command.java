package com.hirannor.hexagonal.infrastructure.command;

import java.time.Instant;

public interface Command {

    CommandId id();

    static Instant now() {
        return Instant.now();
    }

}
