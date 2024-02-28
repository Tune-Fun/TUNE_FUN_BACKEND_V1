package com.tune_fun.v1.base.docker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;

import java.util.Collections;

public class MariaDbTestContainer implements SqlTestContainer {

    private static final Logger log = LoggerFactory.getLogger(MariaDbTestContainer.class);

    private MariaDBContainer<?> mariaDBContainer;

    @Override
    public void destroy() {
        if (null != mariaDBContainer && mariaDBContainer.isRunning()) mariaDBContainer.stop();
    }

    @Override
    public void afterPropertiesSet() {
        if (null == mariaDBContainer) {
            mariaDBContainer =
                new MariaDBContainer<>("mariadb:11.2.2")
                    .withDatabaseName("jhipsterDemo")
                    .withTmpFs(Collections.singletonMap("/testtmpfs", "rw"))
                    .withLogConsumer(new Slf4jLogConsumer(log))
                    .withReuse(true);
        }
        if (!mariaDBContainer.isRunning()) mariaDBContainer.start();
    }

    @Override
    public JdbcDatabaseContainer<?> getTestContainer() {
        return mariaDBContainer;
    }
}
