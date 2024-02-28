package com.tune_fun.v1.base.docker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.utility.DockerImageName;

import static org.testcontainers.containers.localstack.LocalStackContainer.Service.S3;
import static org.testcontainers.containers.localstack.LocalStackContainer.Service.SECRETSMANAGER;

public class LocalStackTestContainer implements InitializingBean, DisposableBean {

    private static final DockerImageName LOCAL_STACK_IMAGE = DockerImageName.parse("localstack/localstack:latest");
    private static final String LOCAL_STACK_S3_BUCKET_NAME = "test";
    private static final String LOCAL_STACK_SECRETS_MANAGER_SECRET_NAME = "test_secret";

    private static final Logger log = LoggerFactory.getLogger(LocalStackTestContainer.class);
    
    private LocalStackContainer localStackContainer;

    @Override
    public void destroy() throws Exception {
        if (null != localStackContainer && localStackContainer.isRunning()) localStackContainer.stop();
    }

    @Override
    public void afterPropertiesSet() throws Exception {

        if (null == localStackContainer) {
            localStackContainer = new LocalStackContainer(LOCAL_STACK_IMAGE)
                    .withServices(S3, SECRETSMANAGER);
        }

        if (!localStackContainer.isRunning()) localStackContainer.start();
    }
}
