package com.tune_fun.v1.common.config.aws;

import com.tune_fun.v1.common.config.annotation.OnlyDevelopmentConfiguration;
import io.awspring.cloud.sqs.config.SqsMessageListenerContainerFactory;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

import static software.amazon.awssdk.regions.Region.AP_NORTHEAST_2;

@Slf4j
@OnlyDevelopmentConfiguration
public class AwsSqsConfig {

    @Bean
    @Profile("dev_standalone")
    public SqsAsyncClient sqsAsyncClient() {
        log.info("SqsAsyncClient is created for dev_standalone");
        return SqsAsyncClient.builder()
                .region(AP_NORTHEAST_2)
                .credentialsProvider(AwsCredentialFactory.environment())
                .build();
    }

    @Bean
    @Profile("dev | staging | prod")
    public SqsAsyncClient amazonS3ClientStaging() {
        log.info("SqsAsyncClient is created for dev, staging, prod");
        return SqsAsyncClient.builder()
                .region(AP_NORTHEAST_2)
                .credentialsProvider(AwsCredentialFactory.ec2Instance())
                .build();
    }

    @Bean
    public SqsMessageListenerContainerFactory<Object> defaultSqsListenerContainerFactory() {
        log.info("SqsMessageListenerContainerFactory is created");
        return SqsMessageListenerContainerFactory
                .builder()
                .sqsAsyncClient(sqsAsyncClient())
                .build();
    }

    @Bean
    public SqsTemplate sqsTemplate() {
        log.info("SqsTemplate is created");
        return SqsTemplate.newTemplate(sqsAsyncClient());
    }

}
