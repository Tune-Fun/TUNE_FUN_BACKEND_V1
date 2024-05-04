package com.tune_fun.v1.base.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tune_fun.v1.common.constant.Constants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

/**
 * Abstract class to define and enforce architectural rules for bounded contexts
 * within the application. Subclasses should specify the bounded context package
 * and can extend rules as needed.
 */
public abstract class BoundedContextDependencyRuleTest {

    private static final String ROOT_PACKAGE = "com.tune_fun.v1";
    private static final String DOMAIN_PACKAGE = "domain";
    private static final String APPLICATION_PACKAGE = "application";
    private static final String PORT_PACKAGE = "application.port";
    private static final String SERVICE_PACKAGE = "application.service";
    private static final String ADAPTER_PACKAGE = "adapter";

    public void checkDependencyRule() {
        String rootPackage = fullyBoundedContextPackage(getBoundedContextPackage());
        JavaClasses classesToCheck = new ClassFileImporter().importPackages(rootPackage + "..");
        assertDependency(rootPackage, classesToCheck);
    }

    private static void assertDependency(@NotBlank String rootPackage, @NotNull JavaClasses classesToCheck) {
        checkNoDependencyFromTo(rootPackage, DOMAIN_PACKAGE, APPLICATION_PACKAGE, classesToCheck);

        checkNoDependencyFromTo(rootPackage, DOMAIN_PACKAGE, ADAPTER_PACKAGE, classesToCheck);

        checkNoDependencyFromTo(rootPackage, APPLICATION_PACKAGE, ADAPTER_PACKAGE, classesToCheck);

        checkNoDependencyFromTo(rootPackage, PORT_PACKAGE, SERVICE_PACKAGE, classesToCheck);

        checkNoDependencyFromTo(rootPackage, ADAPTER_PACKAGE, SERVICE_PACKAGE, classesToCheck);
    }

    public static void checkNoDependencyFromTo(
            String rootPackage, String fromPackage, String toPackage, JavaClasses classesToCheck) {
        noClasses()
                .that()
                .resideInAPackage(fullyQualified(rootPackage, fromPackage))
                .should()
                .dependOnClassesThat()
                .resideInAPackage(fullyQualified(rootPackage, toPackage))
                .check(classesToCheck);
    }

    private static String fullyBoundedContextPackage(String boundedContextPackage) {
        return ROOT_PACKAGE + Constants.DOT + boundedContextPackage;
    }

    private static String fullyQualified(String rootPackage, String packageName) {
        return rootPackage + Constants.DOT + packageName + "..";
    }

    public abstract String getBoundedContextPackage();

}
