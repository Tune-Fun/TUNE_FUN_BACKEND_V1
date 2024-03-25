package com.tune_fun.v1.common.config.aws;

import io.awspring.cloud.sqs.config.SqsMessageListenerContainerFactory;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

import static software.amazon.awssdk.regions.Region.AP_NORTHEAST_2;

@Slf4j
@Configuration
public class AwsSqsConfig {

    @Bean("sqsAsyncClient")
    @Profile("dev_standalone")
    public SqsAsyncClient sqsAsyncClient() {
        log.info("SqsAsyncClient is created for dev_standalone");
        return SqsAsyncClient.builder()
                .region(AP_NORTHEAST_2)
                .credentialsProvider(AwsCredentialFactory.environment())
                .build();
    }

    @Bean("sqsAsyncClient")
    @Profile("dev | staging | prod")
    public SqsAsyncClient amazonAsyncClientStaging() {
        log.info("SqsAsyncClient is created for dev, staging, prod");
        return SqsAsyncClient.builder()
                .region(AP_NORTHEAST_2)
                .credentialsProvider(AwsCredentialFactory.ec2Instance())
                .build();
    }

    @Bean
    @DependsOn("sqsAsyncClient")
    public SqsMessageListenerContainerFactory<Object> defaultSqsListenerContainerFactory(SqsAsyncClient sqsAsyncClient) {
        log.info("SqsMessageListenerContainerFactory is created");
        return SqsMessageListenerContainerFactory
                .builder()
                .sqsAsyncClient(sqsAsyncClient)
                .build();
    }

    @Bean
    @DependsOn("sqsAsyncClient")
    public SqsTemplate sqsTemplate(SqsAsyncClient sqsAsyncClient) {
        log.info("SqsTemplate is created");
        return SqsTemplate.newTemplate(sqsAsyncClient);
    }

}
