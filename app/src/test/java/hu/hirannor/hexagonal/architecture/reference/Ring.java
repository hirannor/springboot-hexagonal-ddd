package hu.hirannor.hexagonal.architecture.reference;


import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaClass;

public enum Ring {
    ADAPTER("adapter", "hu.hirannor.hexagonal.adapter.."),
    APPLICATION("application", "hu.hirannor.hexagonal.application.."),
    DOMAIN("domain", "hu.hirannor.hexagonal.domain.."),
    INFRASTRUCTURE(
        "infrastructure",
        "hu.hirannor.hexagonal.infrastructure.."
    ),
    MESSAGING(
        "messaging adapter",
        "hu.hirannor.hexagonal.adapter.messaging.."
    ),
    AUTH_BASIC(
        "authentication basic adapter",
        "hu.hirannor.hexagonal.adapter.authentication.basic.."
    ),
    AUTH_JWT(
        "authentication jwt adapter",
        "hu.hirannor.hexagonal.adapter.authentication.jwt.."
    ),
    JPA("jpa adapter", "hu.hirannor.hexagonal.adapter.persistence.jpa.."),
    JPA_CUSTOMER(
        "jpa customer adapter",
        "hu.hirannor.hexagonal.adapter.persistence.jpa.customer.."
    ),
    IN_MEMORY_CUSTOMER(
        "in memory customer adapter",
        "hu.hirannor.hexagonal.adapter.persistence.inmemory.."
    ),
    REST("web rest adapter", "hu.hirannor.hexagonal.adapter.web.rest.."),
    REST_CUSTOMER(
        "web rest customer adapter",
        "hu.hirannor.hexagonal.adapter.web.rest.customer.."
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
