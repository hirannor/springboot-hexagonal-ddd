package io.github.hirannor.oms.infrastructure.adapter;

import java.lang.annotation.*;

/**
 * Marker annotation for driver adapters.
 *
 * @author Mate Karolyi
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DriverAdapter {
}
