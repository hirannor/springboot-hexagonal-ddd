package io.github.hirannor.oms.architecture.reference;


import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaClass;

public enum Ring {
    ADAPTER("adapter", "io.github.hirannor.oms.adapter.."),
    APPLICATION("application", "io.github.hirannor.oms.application.."),
    DOMAIN("domain", "io.github.hirannor.oms.domain.."),
    INFRASTRUCTURE(
            "infrastructure",
            "io.github.hirannor.oms.infrastructure.."
    ),
    MESSAGING(
            "messaging adapter",
            "io.github.hirannor.oms.adapter.messaging.."
    ),
    AUTH_JWT(
            "authentication jwt adapter",
            "io.github.hirannor.oms.adapter.authentication.jwt.."
    ),
    PAYMENT_MOCK(
            "mock payment adapter",
            "io.github.hirannor.oms.adapter.payment.mock.."
    ),
    PAYMENT_STRIPE(
            "stripe payment adapter",
            "io.github.hirannor.oms.adapter.payment.stripe.."
    ),
    NOTIFICATION_EMAIL(
            "email notification adapter",
            "io.github.hirannor.oms.adapter.notification.email.."
    ),
    NOTIFICATION_SMS(
            "sms notification adapter",
            "io.github.hirannor.oms.adapter.notification.sms.."
    ),
    JPA("jpa adapter", "io.github.hirannor.oms.adapter.persistence.jpa.."),
    JPA_CUSTOMER(
            "jpa customer adapter",
            "io.github.hirannor.oms.adapter.persistence.jpa.customer.."
    ),
    IN_MEMORY_CUSTOMER(
            "in memory customer adapter",
            "io.github.hirannor.oms.adapter.persistence.inmemory.."
    ),
    REST("web rest adapter", "io.github.hirannor.oms.adapter.web.rest.."),
    REST_CUSTOMER(
            "web rest customer adapter",
            "io.github.hirannor.oms.adapter.web.rest.customer.."
    );
    private final String ringName;
    private final String packagePath;

    Ring(final String ringName, final String packagePath) {
        this.ringName = ringName;
        this.packagePath = packagePath;
    }

    public String ringName() {
        return ringName;
    }

    public DescribedPredicate<JavaClass> packagePath() {
        return JavaClass.Predicates.resideInAnyPackage(packagePath);
    }
}
