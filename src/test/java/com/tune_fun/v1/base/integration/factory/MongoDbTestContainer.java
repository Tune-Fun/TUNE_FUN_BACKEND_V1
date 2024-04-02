package com.tune_fun.v1.base.integration.factory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;

import java.util.Collections;

public class MongoDbTestContainer implements InitializingBean, DisposableBean {

    private static final Logger log = LoggerFactory.getLogger(MongoDbTestContainer.class);

    private MongoDBContainer mongodbContainer;

    @Override
    public void destroy() {
        if (null != mongodbContainer && mongodbContainer.isRunning()) mongodbContainer.stop();
    }

    @Override
    public void afterPropertiesSet() {
        if (null == mongodbContainer) mongodbContainer =
                new MongoDBContainer("mongo:7.0.4")
                        .withTmpFs(Collections.singletonMap("/testtmpfs", "rw"))
                        .withLogConsumer(new Slf4jLogConsumer(log))
                        .withReuse(true);
        if (!mongodbContainer.isRunning()) mongodbContainer.start();
    }

    public MongoDBContainer getMongoDBContainer() {
        return mongodbContainer;
    }
}
