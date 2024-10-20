package com.tune_fun.v1.base.integration;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.icegreen.greenmail.user.GreenMailUser;
import com.icegreen.greenmail.util.GreenMail;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.utility.DockerImageName;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.services.kms.KmsClient;
import software.amazon.awssdk.services.kms.model.CreateKeyResponse;
import software.amazon.awssdk.services.kms.model.KeySpec;
import software.amazon.awssdk.services.kms.model.KeyUsageType;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;

import static com.tune_fun.v1.base.integration.MailConfig.SMTP_USERNAME;
import static java.lang.String.format;
import static java.lang.System.setProperty;
import static org.testcontainers.containers.localstack.LocalStackContainer.Service.*;
import static software.amazon.awssdk.auth.credentials.AwsBasicCredentials.create;
import static software.amazon.awssdk.regions.Region.of;
import static software.amazon.awssdk.services.kms.model.OriginType.AWS_KMS;

@Slf4j
@TestConfiguration
@Profile("test_standalone")
public class LocalStackConfig {

    private static final DockerImageName LOCAL_STACK_IMAGE = DockerImageName.parse("localstack/localstack:3.3");
    private static final String LOCAL_STACK_S3_BUCKET_NAME = "test";
    private static final String LOCAL_STACK_SECRETS_MANAGER_SECRET_NAME = "test_secret";


    @Bean(initMethod = "start", destroyMethod = "stop")
    public static LocalStackContainer localStackContainer() {
        return new LocalStackContainer(LOCAL_STACK_IMAGE)
                .withServices(S3, KMS, SECRETSMANAGER);
    }

    @DynamicPropertySource
    public static void properties(DynamicPropertyRegistry registry,
                                  SecretsManagerClient secretsManagerClient,
                                  KmsClient kmsClient) throws JsonProcessingException {
        SecretInfo secretInfo = new ObjectMapper().readValue(getSecretValue(secretsManagerClient).secretString(), SecretInfo.class);
        registry.add("spring.mail.username", secretInfo::gmailUsername);
        registry.add("spring.mail.password", secretInfo::gmailPassword);

    }

    private static GetSecretValueResponse getSecretValue(SecretsManagerClient secretsManagerClient) {
        return secretsManagerClient.getSecretValue(b -> b.secretId(LOCAL_STACK_SECRETS_MANAGER_SECRET_NAME));
    }

    private static String getMailSecret(String username, String password) {
        return format("{\"gmail-username\": \"%s\", \"gmail-password\": \"%s\"}", username, password);
    }


    private void createKeyArn(KmsClient kmsClient) {
        log.info("Creating Test KMS keys");

        CreateKeyResponse testEncryptKeyResponse = kmsClient.createKey(b -> b
                .description("Test Encrypt key")
                .keyUsage(KeyUsageType.ENCRYPT_DECRYPT)
                .origin(AWS_KMS)
        );

        log.info("Test Encrypt key created");
        log.info("Test Encrypt Key Arn: {}", testEncryptKeyResponse.keyMetadata().arn());

        CreateKeyResponse testJwtSignatureResponse = kmsClient.createKey(b -> b
                .description("Test Jwt Signature")
                .keyUsage(KeyUsageType.GENERATE_VERIFY_MAC)
                .keySpec(KeySpec.HMAC_512)
                .origin(AWS_KMS)
        );

        log.info("Test Jwt Signature key created");
        log.info("Test Encrypt Key Arn: {}", testJwtSignatureResponse.keyMetadata().arn());

        setProperty("kms.encrypt-key-arn", testEncryptKeyResponse.keyMetadata().arn());
        setProperty("kms.jwt-signature-arn", testJwtSignatureResponse.keyMetadata().arn());
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

    @Bean
    @DependsOn({"localStackContainer", "greenMail"})
    protected SecretsManagerClient secretsManagerClient(
            LocalStackContainer localStackContainer,
            GreenMail greenMail
    ) {
        SecretsManagerClient secretsManagerClient = SecretsManagerClient.builder()
                .endpointOverride(localStackContainer.getEndpointOverride(SECRETSMANAGER))
                .credentialsProvider(getCredentialsProvider(localStackContainer))
                .region(of(localStackContainer.getRegion()))
                .build();

        secretsManagerClient.createSecret(b -> b.name(LOCAL_STACK_SECRETS_MANAGER_SECRET_NAME));

        GreenMailUser greenMailUser = greenMail.getUserManager().getUser(SMTP_USERNAME);
        String mailSecret = getMailSecret(greenMailUser.getLogin(), greenMailUser.getPassword());

        secretsManagerClient.putSecretValue(b -> b.secretId(LOCAL_STACK_SECRETS_MANAGER_SECRET_NAME)
                .secretString(mailSecret));

        return secretsManagerClient;
    }

    @Bean
    @DependsOn({"localStackContainer"})
    protected KmsClient kmsClient(LocalStackContainer localStackContainer) {
        KmsClient kmsClient = KmsClient.builder()
                .endpointOverride(localStackContainer.getEndpointOverride(KMS))
                .credentialsProvider(getCredentialsProvider(localStackContainer))
                .region(of(localStackContainer.getRegion()))
                .build();

        createKeyArn(kmsClient);
        return kmsClient;
    }

    @NotNull
    private StaticCredentialsProvider getCredentialsProvider(LocalStackContainer localStackContainer) {
        return StaticCredentialsProvider.create(create(localStackContainer.getAccessKey(), localStackContainer.getSecretKey()));
    }

    private record SecretInfo(
            @JsonProperty("gmail-username")
            String gmailUsername,

            @JsonProperty("gmail-password")
            String gmailPassword) {

    }

}
