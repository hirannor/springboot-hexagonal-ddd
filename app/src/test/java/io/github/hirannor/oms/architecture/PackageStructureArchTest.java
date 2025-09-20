package io.github.hirannor.oms.architecture;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.library.Architectures;
import org.junit.jupiter.api.DisplayName;

import static io.github.hirannor.oms.architecture.reference.Ring.*;

@AnalyzeClasses(
        packages = "io.github.hirannor.oms",
        importOptions = {
                ImportOption.DoNotIncludeTests.class,
                ImportOption.DoNotIncludeJars.class,
        }
)
@DisplayName("PackageStructureArch")
class PackageStructureArchTest {

    private static final String SPRING_BOOT_APPLICATION_CLASS_NAME =
            "OrderManagementSystemApplication";
    private static final String HEXAGONAL_APP = "hexagonal application";

    PackageStructureArchTest() {
    }

    @ArchTest
    @DisplayName("should respect onion rules")
    void shouldRespectOnionRules(final JavaClasses classes) {
        onion()
                .whereLayer(ADAPTER.ringName()).mayOnlyBeAccessedByLayers(HEXAGONAL_APP)
                .whereLayer(APPLICATION.ringName()).mayOnlyBeAccessedByLayers(ADAPTER.ringName(), HEXAGONAL_APP)
                .whereLayer(DOMAIN.ringName()).mayOnlyBeAccessedByLayers(ADAPTER.ringName(), APPLICATION.ringName())
                .whereLayer(INFRASTRUCTURE.ringName())
                .mayOnlyBeAccessedByLayers(ADAPTER.ringName(), APPLICATION.ringName(), DOMAIN.ringName())
                .check(classes);
    }

    @ArchTest
    @DisplayName("should have adapter packages separated")
    void shouldHaveAdapterPackagesSeparated(final JavaClasses classes) {
        adapters()
                .whereLayer(MESSAGING.ringName())
                .mayOnlyBeAccessedByLayers(APPLICATION.ringName(), HEXAGONAL_APP)
                .whereLayer(JPA_CUSTOMER.ringName())
                .mayOnlyBeAccessedByLayers(JPA.ringName())
                .whereLayer(IN_MEMORY_CUSTOMER.ringName())
                .mayOnlyBeAccessedByLayers(APPLICATION.ringName(), HEXAGONAL_APP)
                .whereLayer(REST_CUSTOMER.ringName())
                .mayOnlyBeAccessedByLayers(REST.ringName())
                .whereLayer(AUTH_JWT.ringName())
                .mayOnlyBeAccessedByLayers(APPLICATION.ringName(), HEXAGONAL_APP)
                .whereLayer(PAYMENT_MOCK.ringName())
                .mayOnlyBeAccessedByLayers(APPLICATION.ringName(), HEXAGONAL_APP)
                .whereLayer(PAYMENT_STRIPE.ringName())
                .mayOnlyBeAccessedByLayers(APPLICATION.ringName(), HEXAGONAL_APP)
                .whereLayer(NOTIFICATION_SMS.ringName())
                .mayOnlyBeAccessedByLayers(APPLICATION.ringName(), HEXAGONAL_APP)
                .whereLayer(NOTIFICATION_EMAIL.ringName())
                .mayOnlyBeAccessedByLayers(APPLICATION.ringName(), HEXAGONAL_APP)
                .check(classes);
    }

    private Architectures.LayeredArchitecture adapters() {
        return Architectures.layeredArchitecture().consideringAllDependencies()
                .layer(APPLICATION.ringName()).definedBy(APPLICATION.packagePath())
                .layer(MESSAGING.ringName()).definedBy(MESSAGING.packagePath())
                .layer(JPA.ringName()).definedBy(JPA.packagePath())
                .layer(JPA_CUSTOMER.ringName()).definedBy(JPA_CUSTOMER.packagePath())
                .layer(IN_MEMORY_CUSTOMER.ringName()).definedBy(IN_MEMORY_CUSTOMER.packagePath())
                .layer(REST.ringName()).definedBy(REST.packagePath())
                .layer(REST_CUSTOMER.ringName()).definedBy(REST_CUSTOMER.packagePath())
                .layer(AUTH_JWT.ringName()).definedBy(AUTH_JWT.packagePath())
                .layer(PAYMENT_STRIPE.ringName()).definedBy(PAYMENT_STRIPE.packagePath())
                .layer(PAYMENT_MOCK.ringName()).definedBy(PAYMENT_MOCK.packagePath())
                .layer(NOTIFICATION_SMS.ringName()).definedBy(NOTIFICATION_SMS.packagePath())
                .layer(NOTIFICATION_EMAIL.ringName()).definedBy(NOTIFICATION_EMAIL.packagePath())
                .layer(HEXAGONAL_APP).definedBy(JavaClass.Predicates.simpleName(SPRING_BOOT_APPLICATION_CLASS_NAME));
    }

    private Architectures.LayeredArchitecture onion() {
        return Architectures.layeredArchitecture().consideringAllDependencies()
                .layer(ADAPTER.ringName()).definedBy(ADAPTER.packagePath())
                .layer(APPLICATION.ringName()).definedBy(APPLICATION.packagePath())
                .layer(DOMAIN.ringName()).definedBy(DOMAIN.packagePath())
                .layer(INFRASTRUCTURE.ringName()).definedBy(INFRASTRUCTURE.packagePath())
                .layer(HEXAGONAL_APP).definedBy(JavaClass.Predicates.simpleName(SPRING_BOOT_APPLICATION_CLASS_NAME));
    }

}
