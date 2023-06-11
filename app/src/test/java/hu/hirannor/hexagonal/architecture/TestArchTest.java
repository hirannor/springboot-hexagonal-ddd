package hu.hirannor.hexagonal.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.syntax.elements.GivenClassesConjunction;
import com.tngtech.archunit.lang.syntax.elements.GivenMethodsConjunction;
import hu.hirannor.hexagonal.TestContainerBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Configuration;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.methods;

@AnalyzeClasses(
        packages = "hu.hirannor.hexagonal",
        importOptions = ImportOption.OnlyIncludeTests.class
)
@DisplayName("TestArch")
class TestArchTest {

    private final GivenClassesConjunction testClasses;
    private final GivenMethodsConjunction testCases;

    TestArchTest() {
        testClasses =
                classes()
                        .that()
                        .resideOutsideOfPackage(
                                "hu.hirannor.hexagonal.architecture.."
                        )
                        .and()
                        .areTopLevelClasses()
                        .and()
                        .areNotAnnotatedWith(Configuration.class)
                        .and()
                        .areNotAnnotatedWith(TestConfiguration.class)
                        .and()
                        .areNotAssignableTo(TestContainerBase.class)
                        .and()
                        .areNotRecords();

        testCases =
                methods()
                        .that()
                        .areDeclaredInClassesThat()
                        .resideOutsideOfPackage(
                                "hu.hirannor.hexagonal.architecture.."
                        )
                        .and()
                        .areDeclaredInClassesThat()
                        .areTopLevelClasses()
                        .and()
                        .areDeclaredInClassesThat()
                        .areNotAnnotatedWith(Configuration.class)
                        .and()
                        .areDeclaredInClassesThat()
                        .areNotAnnotatedWith(TestConfiguration.class)
                        .and()
                        .areDeclaredInClassesThat()
                        .areNotRecords()
                        .and()
                        .arePackagePrivate()
                        .and()
                        .areNotAnnotatedWith(BeforeEach.class);
    }

    @ArchTest
    void shouldNotHavePublicTestClasses(final JavaClasses classes) {
        testClasses
                .should().notBePublic().as("Test classes shouldn't be public")
                .because("they not needed to be exposed more than necessary - which is package private scope.")
                .check(classes);
    }

    @ArchTest
    void shouldNotHavePublicTestCases(final JavaClasses classes) {
        testCases
                .should().notBePublic()
                .as("Test cases shouldn't be public")
                .because("they not needed to be exposed more than necessary - which is package private scope.")
                .check(classes);
    }

    @ArchTest
    void shouldDescribeTestClasses(final JavaClasses classes) {
        testClasses
                .should().beAnnotatedWith(DisplayName.class)
                .as("Test classes should be annotated with @DisplayName")
                .because("they should provide a readable summary")
                .check(classes);
    }

    @ArchTest
    void shouldDescribeTestCases(final JavaClasses classes) {
        testCases
                .should().beAnnotatedWith(DisplayName.class)
                .as("Test cases should be annotated with @DisplayName")
                .because("they should provide a readable summary")
                .check(classes);
    }
}
