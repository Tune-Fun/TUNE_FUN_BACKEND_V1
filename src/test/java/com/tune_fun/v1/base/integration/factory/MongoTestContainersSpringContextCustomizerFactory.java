package com.tune_fun.v1.base.integration.factory;

import com.tune_fun.v1.base.integration.EmbeddedMongo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.test.context.ContextConfigurationAttributes;
import org.springframework.test.context.ContextCustomizer;
import org.springframework.test.context.ContextCustomizerFactory;

import java.util.List;

public class MongoTestContainersSpringContextCustomizerFactory implements ContextCustomizerFactory {

    private final Logger log = LoggerFactory.getLogger(MongoTestContainersSpringContextCustomizerFactory.class);

    private static MongoDbTestContainer mongoDbBean;

    @Override
    public ContextCustomizer createContextCustomizer(Class<?> testClass, List<ContextConfigurationAttributes> configAttributes) {
        return (context, mergedConfig) -> {
            ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
            TestPropertyValues testValues = TestPropertyValues.empty();
            EmbeddedMongo mongoAnnotation = AnnotatedElementUtils.findMergedAnnotation(testClass, EmbeddedMongo.class);
            if (null != mongoAnnotation) {
                log.debug("detected the EmbeddedMongo annotation on class {}", testClass.getName());
                log.info("Warming up the mongo database");
                if (null == mongoDbBean) {
                    mongoDbBean = beanFactory.createBean(MongoDbTestContainer.class);
                    beanFactory.registerSingleton(MongoDbTestContainer.class.getName(), mongoDbBean);
                }
                testValues = testValues.and("spring.data.mongodb.uri=" + mongoDbBean.getMongoDBContainer().getReplicaSetUrl());
            }
            testValues.applyTo(context);
        };
    }
}
