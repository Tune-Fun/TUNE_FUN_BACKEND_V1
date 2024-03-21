package com.tune_fun.v1.base.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

public abstract class BoundedContextDependencyRuleTest {

    private static final String ROOT_PACKAGE = "com.tune_fun.v1";
    private static final String DOMAIN_PACKAGE = "domain";
    private static final String APPLICATION_PACKAGE = "application";
    private static final String PORT_PACKAGE = "application.port";
    private static final String SERVICE_PACKAGE = "application.service";
    private static final String ADAPTER_PACKAGE = "adapter";

    private static void assertDependency(String rootPackage, JavaClasses classesToCheck) {
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
        return ROOT_PACKAGE + "." + boundedContextPackage;
    }

    private static String fullyQualified(String rootPackage, String packageName) {
        return rootPackage + '.' + packageName + "..";
    }

    public abstract String getBoundedContextPackage();

    public void checkDependencyRule() {
        String rootPackage = fullyBoundedContextPackage(getBoundedContextPackage());
        String importPackages = rootPackage + "..";
        JavaClasses classesToCheck = new ClassFileImporter().importPackages(importPackages);
        assertDependency(rootPackage, classesToCheck);
    }

}
