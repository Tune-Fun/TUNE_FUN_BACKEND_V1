package com.tune_fun.v1.common.config.aws;

import com.tune_fun.v1.common.config.annotation.OnlyDevelopmentConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.services.s3.S3Client;

import static software.amazon.awssdk.regions.Region.AP_NORTHEAST_2;

@Slf4j
@OnlyDevelopmentConfiguration
public class AwsS3Config {

    @Bean
    @Profile("dev_standalone")
    public S3Client amazonS3Client() {
        log.info("S3Client is created for dev_standalone");
        return S3Client.builder()
                .region(AP_NORTHEAST_2)
                .credentialsProvider(AwsCredentialFactory.environment())
                .build();
    }

    @Bean
    @Profile("dev | staging | prod")
    public S3Client amazonS3ClientStaging() {
        log.info("S3Client is created for dev, staging, prod");
        return S3Client.builder()
                .region(AP_NORTHEAST_2)
                .credentialsProvider(AwsCredentialFactory.ec2Instance())
                .build();
    }


}
