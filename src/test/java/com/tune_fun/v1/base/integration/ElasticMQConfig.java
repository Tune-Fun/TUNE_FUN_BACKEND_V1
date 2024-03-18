package com.tune_fun.v1.base.integration;

import lombok.extern.slf4j.Slf4j;
import org.elasticmq.rest.sqs.SQSRestServer;
import org.elasticmq.rest.sqs.SQSRestServerBuilder;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

import static software.amazon.awssdk.auth.credentials.AwsBasicCredentials.create;
import static software.amazon.awssdk.regions.Region.AP_NORTHEAST_2;

@Slf4j
@TestConfiguration
public class ElasticMQConfig {

    private UriComponents elasticMQLocalSqsUri() {
        return UriComponentsBuilder.newInstance()
                .scheme("http")
                .host("0.0.0.0")
                .port(9324)
                .build()
                .encode();
    }

    @Bean(destroyMethod = "stopAndWait")
    public SQSRestServer sqsRestServer() {
        return SQSRestServerBuilder
                .withInterface("0.0.0.0")
                .withPort(9324)
                .start();
    }

    @Primary
    @Bean
    @Profile("test_standalone")
    public SqsAsyncClient sqsAsyncClient() {
        log.info("SqsAsyncClient is created for test_standalone");
        return SqsAsyncClient.builder()
                .region(AP_NORTHEAST_2)
                .credentialsProvider(getCredentialsProvider())
                .endpointOverride(elasticMQLocalSqsUri().toUri())
                .build();
    }

    private static AwsCredentialsProvider getCredentialsProvider() {
        return StaticCredentialsProvider.create(create("X", "X"));
    }

}
