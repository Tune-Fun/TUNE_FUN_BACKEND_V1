package com.tune_fun.v1.base.integration;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

import static org.springframework.test.context.support.TestPropertySourceUtils.addInlinedPropertiesToEnvironment;
import static org.springframework.test.util.TestSocketUtils.findAvailableTcpPort;

public class RandomPortInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        addInlinedPropertiesToEnvironment(applicationContext, "spring.mail.port=" + findAvailableTcpPort());
        addInlinedPropertiesToEnvironment(applicationContext, "elasticmq.port=" + findAvailableTcpPort());
    }
}
