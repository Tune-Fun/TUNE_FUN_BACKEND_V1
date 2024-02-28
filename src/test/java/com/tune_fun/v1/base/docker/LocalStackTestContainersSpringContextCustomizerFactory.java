package com.tune_fun.v1.base.docker;

import com.tune_fun.v1.base.annotation.EmbeddedAws;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.test.context.ContextConfigurationAttributes;
import org.springframework.test.context.ContextCustomizer;
import org.springframework.test.context.ContextCustomizerFactory;
import org.testcontainers.containers.KafkaContainer;

import java.util.List;

public class LocalStackTestContainersSpringContextCustomizerFactory implements ContextCustomizerFactory {

    private final Logger log = LoggerFactory.getLogger(LocalStackTestContainersSpringContextCustomizerFactory.class);

    private static LocalStackTestContainer localStackBean;

    @Override
    public ContextCustomizer createContextCustomizer(Class<?> testClass, List<ContextConfigurationAttributes> configAttributes) {
        return (context, mergedConfig) -> {
            ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
            TestPropertyValues testValues = TestPropertyValues.empty();
            EmbeddedAws kafkaAnnotation = AnnotatedElementUtils.findMergedAnnotation(testClass, EmbeddedAws.class);
            if (null != kafkaAnnotation) {
                log.debug("detected the EmbeddedKafka annotation on class {}", testClass.getName());
                log.info("Warming up the kafka broker");
                if (null == localStackBean) {
                    localStackBean = beanFactory.createBean(LocalStackTestContainer.class);
                    beanFactory.registerSingleton(LocalStackTestContainer.class.getName(), localStackBean);
                }
                testValues =
                        testValues.and(

                        );
            }
            testValues.applyTo(context);
        };
    }
}
