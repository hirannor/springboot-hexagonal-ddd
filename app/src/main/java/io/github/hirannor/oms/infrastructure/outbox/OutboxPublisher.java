package io.github.hirannor.oms.infrastructure.outbox;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface OutboxPublisher {
}
