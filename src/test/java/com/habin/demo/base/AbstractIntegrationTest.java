package com.habin.demo.base;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.habin.demo.DemoApplication;
import com.habin.demo.base.aws.LocalStackConfig;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

@Import({LocalStackConfig.class})
@SpringBootTest(
        classes = DemoApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = "spring.main.allow-bean-definition-overriding=true"
)
@ActiveProfiles("test_standalone")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public abstract class AbstractIntegrationTest {

    protected JsonMapper jsonMapper;

    public AbstractIntegrationTest() {
        this.jsonMapper = getJsonMapper();
    }

    private static JsonMapper getJsonMapper() {
        return JsonMapper.builder()
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .build();
    }

    public <T> String toJson(T data) throws JsonProcessingException {
        return jsonMapper.writeValueAsString(data);
    }

    private static final String ENV_TZ = "TZ";
    private static final String ASIA_SEOUL = "Asia/Seoul";

    private static final String MYSQL_IMAGE_NAME = "mariadb:latest";
    @Container
    public static final MariaDBContainer<?> MARIADB_CONTAINER =
            new MariaDBContainer<>(DockerImageName.parse(MYSQL_IMAGE_NAME))
                    .withEnv(ENV_TZ, ASIA_SEOUL)
                    .withEnv("MARIADB_DATABASE", "test")
                    .withEnv("MARIADB_USER", "test")
                    .withEnv("MARIADB_PASSWORD", "test")
                    .withCommand(
                            "--character-set-server=utf8mb4",
                            "--collation-server=utf8mb4_unicode_ci",
                            "--skip-character-set-client-handshake",
                            "--default-time-zone=+09:00"
                    );

    private static final String REDIS_IMAGE_NAME = "redis:latest";
    @Container
    public static final GenericContainer<?> REDIS_CONTAINER =
            new GenericContainer<>(DockerImageName.parse(REDIS_IMAGE_NAME))
                    .withEnv(ENV_TZ, ASIA_SEOUL)
                    .withExposedPorts(6379);

    private static final DockerImageName KAFKA_IMAGE = DockerImageName.parse("confluentinc/cp-kafka:latest");

    @Container
    public static final KafkaContainer KAFKA_CONTAINER = new KafkaContainer(KAFKA_IMAGE);

    @DynamicPropertySource
    public static void properties(DynamicPropertyRegistry registry) {
        MARIADB_CONTAINER.start();
        registry.add("spring.datasource.url",
                () -> String.format("jdbc:mariadb://localhost:%d/%s", MARIADB_CONTAINER.getFirstMappedPort(), MARIADB_CONTAINER.getDatabaseName()));
        registry.add("spring.datasource.username", MARIADB_CONTAINER::getUsername);
        registry.add("spring.datasource.password", MARIADB_CONTAINER::getPassword);

        REDIS_CONTAINER.start();
        registry.add("spring.data.redis.host", REDIS_CONTAINER::getHost);
        registry.add("spring.data.redis.port", () -> REDIS_CONTAINER.getMappedPort(6379).toString());
        registry.add("spring.data.redis.database", () -> 0);

        KAFKA_CONTAINER.start();
        registry.add("spring.kafka.bootstrap-servers", KAFKA_CONTAINER::getBootstrapServers);
        registry.add("spring.kafka.consumer.auto-offset-reset", () -> "earliest");
        registry.add("spring.kafka.consumer.group-id", () -> "test");
        registry.add("spring.kafka.consumer.key-deserializer", () -> "org.apache.kafka.common.serialization.StringDeserializer");
        registry.add("spring.kafka.consumer.value-deserializer", () -> "org.apache.kafka.common.serialization.StringDeserializer");
        registry.add("spring.kafka.producer.key-serializer", () -> "org.apache.kafka.common.serialization.StringSerializer");
        registry.add("spring.kafka.producer.value-serializer", () -> "org.apache.kafka.common.serialization.StringSerializer");
    }

}
