package com.tune_fun.v1.base.docker;

import com.tune_fun.v1.base.annotation.EmbeddedRedis;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.test.context.ContextConfigurationAttributes;
import org.springframework.test.context.ContextCustomizer;
import org.springframework.test.context.ContextCustomizerFactory;

import java.util.List;

public class RedisTestContainersSpringContextCustomizerFactory implements ContextCustomizerFactory {

    private final Logger log = LoggerFactory.getLogger(RedisTestContainersSpringContextCustomizerFactory.class);

    private static RedisTestContainer redisBean;

    @Override
    public ContextCustomizer createContextCustomizer(Class<?> testClass, List<ContextConfigurationAttributes> configAttributes) {
        return (context, mergedConfig) -> {
            ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
            TestPropertyValues testValues = TestPropertyValues.empty();
            EmbeddedRedis redisAnnotation = AnnotatedElementUtils.findMergedAnnotation(testClass, EmbeddedRedis.class);
            if (null != redisAnnotation) {
                log.debug("detected the EmbeddedRedis annotation on class {}", testClass.getName());
                log.info("Warming up the redis database");
                if (null == redisBean) {
                    redisBean = beanFactory.createBean(RedisTestContainer.class);
                    beanFactory.registerSingleton(RedisTestContainer.class.getName(), redisBean);
                }
                testValues =
                        testValues.and(
                                "spring.data.redis.url=redis://" +
                                        redisBean.getRedisContainer().getHost() +
                                        ":" +
                                        redisBean.getRedisContainer().getMappedPort(6379)
                        );
            }
            testValues.applyTo(context);
        };
    }
}
