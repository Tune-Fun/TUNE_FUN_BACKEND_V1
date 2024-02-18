package com.tune_fun.v1.base.aws;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.user.GreenMailUser;
import com.icegreen.greenmail.util.ServerSetupTest;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.junit.Rule;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.utility.DockerImageName;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;

import static java.lang.String.format;
import static org.testcontainers.containers.localstack.LocalStackContainer.Service.S3;
import static org.testcontainers.containers.localstack.LocalStackContainer.Service.SECRETSMANAGER;
import static software.amazon.awssdk.auth.credentials.AwsBasicCredentials.create;
import static software.amazon.awssdk.regions.Region.of;

@Slf4j
@TestConfiguration
@Profile("test_standalone")
public class LocalStackConfig {

    private static final DockerImageName LOCAL_STACK_IMAGE = DockerImageName.parse("localstack/localstack:latest");
    private static final String LOCAL_STACK_S3_BUCKET_NAME = "test";
    private static final String LOCAL_STACK_SECRETS_MANAGER_SECRET_NAME = "test_secret";

    public static final String SMTP_USERNAME = "habin";
    public static final String SMTP_PASSWORD = "qpalzm0915()";

    @RegisterExtension
    protected static GreenMailExtension smtp = new GreenMailExtension(ServerSetupTest.SMTP)
            .withConfiguration(GreenMailConfiguration.aConfig().withUser(SMTP_USERNAME, SMTP_PASSWORD))
            .withPerMethodLifecycle(false);

    @Bean(initMethod = "start", destroyMethod = "stop")
    public static LocalStackContainer localStackContainer() {
        return new LocalStackContainer(LOCAL_STACK_IMAGE)
                .withServices(S3, SECRETSMANAGER);
    }

    @Bean
    @DependsOn("localStackContainer")
    protected S3Client s3Client(LocalStackContainer localStackContainer) {
        S3Client s3Client = S3Client.builder()
                .endpointOverride(localStackContainer.getEndpointOverride(S3))
                .credentialsProvider(getCredentialsProvider(localStackContainer))
                .region(of(localStackContainer.getRegion()))
                .build();

        s3Client.createBucket(builder -> builder.bucket(LOCAL_STACK_S3_BUCKET_NAME));
        return s3Client;
    }

    @Primary
    @Bean
    @DependsOn("localStackContainer")
    protected SecretsManagerClient secretsManagerClient(LocalStackContainer localStackContainer) {
        SecretsManagerClient secretsManagerClient = SecretsManagerClient.builder()
                .endpointOverride(localStackContainer.getEndpointOverride(SECRETSMANAGER))
                .credentialsProvider(getCredentialsProvider(localStackContainer))
                .region(of(localStackContainer.getRegion()))
                .build();

        GreenMailUser smtpUser = smtp.getUserManager().getUser(SMTP_USERNAME);

        log.info("Creating secret with name: {}", LOCAL_STACK_SECRETS_MANAGER_SECRET_NAME);
        secretsManagerClient.createSecret(b -> b.name(LOCAL_STACK_SECRETS_MANAGER_SECRET_NAME));

        String mailSecret = getMailSecret(smtpUser);
        log.info("Putting secret value for secret with name: {}, secret: {}", LOCAL_STACK_SECRETS_MANAGER_SECRET_NAME, mailSecret);
        secretsManagerClient.putSecretValue(b -> b.secretId(LOCAL_STACK_SECRETS_MANAGER_SECRET_NAME)
                .secretString(mailSecret));

        GetSecretValueResponse secretValue = getSecretValue(secretsManagerClient);
        log.info("Got secret value for secret with name: {}, secret: {}", LOCAL_STACK_SECRETS_MANAGER_SECRET_NAME, secretValue.secretString());

        return secretsManagerClient;
    }

    @NotNull
    private StaticCredentialsProvider getCredentialsProvider(LocalStackContainer localStackContainer) {
        return StaticCredentialsProvider.create(create(localStackContainer.getAccessKey(), localStackContainer.getSecretKey()));
    }

    @DynamicPropertySource
    public static void properties(DynamicPropertyRegistry registry,
                                  SecretsManagerClient secretsManagerClient) throws JsonProcessingException {
        SecretInfo secretInfo = new ObjectMapper().readValue(getSecretValue(secretsManagerClient).secretString(), SecretInfo.class);
        registry.add("spring.mail.username", secretInfo::gmailUsername);
        registry.add("spring.mail.password", secretInfo::gmailPassword);
    }

    private static GetSecretValueResponse getSecretValue(SecretsManagerClient secretsManagerClient) {
        return secretsManagerClient.getSecretValue(b -> b.secretId(LOCAL_STACK_SECRETS_MANAGER_SECRET_NAME));
    }

    private static String getMailSecret(GreenMailUser smtpUser) {
        return format("{\"gmail-username\": \"%s\", \"gmail-password\": \"%s\"}", smtpUser.getLogin(), smtpUser.getPassword());
    }

    private record SecretInfo(
            @JsonProperty("gmail-username")
            String gmailUsername ,

            @JsonProperty("gmail-password")
            String gmailPassword) {

    }

}
