package com.tune_fun.v1.base.integration.factory;

import com.tune_fun.v1.base.integration.EmbeddedSQL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.test.context.ContextConfigurationAttributes;
import org.springframework.test.context.ContextCustomizer;
import org.springframework.test.context.ContextCustomizerFactory;

import java.util.List;

public class SqlTestContainersSpringContextCustomizerFactory implements ContextCustomizerFactory {

    private final Logger log = LoggerFactory.getLogger(SqlTestContainersSpringContextCustomizerFactory.class);

    private static SqlTestContainer sqlTestContainer;

    @Override
    @SuppressWarnings("unchecked")
    public ContextCustomizer createContextCustomizer(Class<?> testClass, List<ContextConfigurationAttributes> configAttributes) {
        return (context, mergedConfig) -> {
            ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
            TestPropertyValues testValues = TestPropertyValues.empty();
            EmbeddedSQL sqlAnnotation = AnnotatedElementUtils.findMergedAnnotation(testClass, EmbeddedSQL.class);
            if (null != sqlAnnotation) {
                log.debug("detected the EmbeddedSQL annotation on class {}", testClass.getName());
                log.info("Warming up the sql database");
                if (null == sqlTestContainer) {
                    try {
                        Class<? extends SqlTestContainer> containerClass = (Class<? extends SqlTestContainer>) Class.forName(
                            this.getClass().getPackageName() + ".PostgreSqlTestContainer");
                        sqlTestContainer = beanFactory.createBean(containerClass);
                        beanFactory.registerSingleton(containerClass.getName(), sqlTestContainer);
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
                testValues = testValues.and("spring.datasource.url=" + sqlTestContainer.getTestContainer().getJdbcUrl());
                testValues = testValues.and("spring.datasource.username=" + sqlTestContainer.getTestContainer().getUsername());
                testValues = testValues.and("spring.datasource.password=" + sqlTestContainer.getTestContainer().getPassword());
            }
            testValues.applyTo(context);
        };
    }
}
