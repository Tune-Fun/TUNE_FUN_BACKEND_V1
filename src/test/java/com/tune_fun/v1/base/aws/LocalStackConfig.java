package com.tune_fun.v1.base.aws;

import org.jetbrains.annotations.NotNull;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.utility.DockerImageName;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@TestConfiguration
@Profile("test_standalone")
public class LocalStackConfig {

    private static final DockerImageName LOCAL_STACK_IMAGE = DockerImageName.parse("localstack/localstack:latest");
    private static final String LOCAL_STACK_S3_BUCKET_NAME = "test";

    @Bean(initMethod = "start", destroyMethod = "stop")
    public static LocalStackContainer localStackContainer() {
        return new LocalStackContainer(LOCAL_STACK_IMAGE)
                .withServices(LocalStackContainer.Service.S3, LocalStackContainer.Service.KMS);
    }

    @Bean
    @DependsOn("localStackContainer")
    protected S3Client s3Client(LocalStackContainer localStackContainer) {
        S3Client s3Client = S3Client.builder()
                .endpointOverride(localStackContainer.getEndpointOverride(LocalStackContainer.Service.S3))
                .credentialsProvider(getCredentialsProvider(localStackContainer))
                .region(Region.of(localStackContainer.getRegion()))
                .build();

        s3Client.createBucket(builder -> builder.bucket(LOCAL_STACK_S3_BUCKET_NAME));
        return s3Client;
    }

    @NotNull
    private StaticCredentialsProvider getCredentialsProvider(LocalStackContainer localStackContainer) {
        return StaticCredentialsProvider.create(
                AwsBasicCredentials.create(
                        localStackContainer.getAccessKey(),
                        localStackContainer.getSecretKey()
                )
        );
    }

    @DynamicPropertySource
    public static void properties(DynamicPropertyRegistry registry, LocalStackContainer localStackContainer) {
        registry.add("aws.access-key-id", localStackContainer::getAccessKey);
        registry.add("aws.secret-access-key", localStackContainer::getSecretKey);
        registry.add("aws.default-region", localStackContainer::getRegion);
        registry.add("aws.s3-bucket-name", () -> LOCAL_STACK_S3_BUCKET_NAME);
    }

}
