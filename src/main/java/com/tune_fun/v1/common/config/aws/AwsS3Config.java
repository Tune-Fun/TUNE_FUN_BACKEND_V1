package com.tune_fun.v1.common.config.aws;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.services.s3.S3Client;

import static software.amazon.awssdk.regions.Region.AP_NORTHEAST_2;

@Profile("!test & !test_standalone")
@Configuration
public class AwsS3Config {

    @Bean
    @Profile("dev | dev_standalone")
    public S3Client amazonS3Client() {
        return S3Client.builder()
                .region(AP_NORTHEAST_2)
                .credentialsProvider(AwsCredentialFactory.environment())
                .build();
    }

    @Bean
    @Profile("staging | prod")
    public S3Client amazonS3ClientStaging() {
        return S3Client.builder()
                .region(AP_NORTHEAST_2)
                .credentialsProvider(AwsCredentialFactory.ec2Instance())
                .build();
    }


}
