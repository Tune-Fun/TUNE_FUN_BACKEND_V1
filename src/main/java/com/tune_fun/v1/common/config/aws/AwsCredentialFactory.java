package com.tune_fun.v1.common.config.aws;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.auth.credentials.InstanceProfileCredentialsProvider;

@UtilityClass
@Slf4j
public class AwsCredentialFactory {

    public AwsCredentialsProvider provideDefault() {
        log.info("Get AWS Credential From Default Credential Provider Chain.");
        return DefaultCredentialsProvider.create();
    }

    public AwsCredentialsProvider environment() {
        log.info("Get AWS Credential From Environment Variable.");
        return EnvironmentVariableCredentialsProvider.create();
    }

    public AwsCredentialsProvider ec2Instance() {
        log.info("Get AWS Credential From EC2 Instance Linked Role.");
        return InstanceProfileCredentialsProvider.create();
    }

}
