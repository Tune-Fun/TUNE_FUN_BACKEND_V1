package com.tune_fun.v1.base.integration;

import com.tune_fun.v1.common.property.EventProperty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticmq.rest.sqs.SQSRestServer;
import org.elasticmq.rest.sqs.SQSRestServerBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
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
@Profile("test_standalone")
@RequiredArgsConstructor
public class ElasticMQConfig {

    private final EventProperty eventProperty;

    @Value("${elasticmq.port}")
    private int elasticMqPort;

    private UriComponents elasticMQLocalSqsUri() {
        return UriComponentsBuilder.newInstance()
                .scheme("http")
                .host("localhost")
                .port(elasticMqPort)
                .build()
                .encode();
    }

    @Bean(destroyMethod = "stopAndWait")
    public SQSRestServer sqsRestServer() {
        return SQSRestServerBuilder
                .withInterface("0.0.0.0")
                .withPort(elasticMqPort)
                .start();
    }

    @Bean
    @Profile("test_standalone")
    public SqsAsyncClient sqsAsyncClient() {
        log.info("SqsAsyncClient is created for test_standalone");
        SqsAsyncClient sqsAsyncClient = SqsAsyncClient.builder()
                .region(AP_NORTHEAST_2)
                .credentialsProvider(getCredentialsProvider())
                .endpointOverride(elasticMQLocalSqsUri().toUri())
                .build();

        eventProperty.sqs().values().forEach(sqs -> sqsAsyncClient.createQueue(r -> r.queueName(sqs.queueName())));
        return sqsAsyncClient;
    }

    private static AwsCredentialsProvider getCredentialsProvider() {
        return StaticCredentialsProvider.create(create("X", "X"));
    }

}
