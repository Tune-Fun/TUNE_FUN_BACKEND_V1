package com.tune_fun.v1.base.integration;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

import javax.sql.DataSource;

import static org.springframework.boot.jdbc.DataSourceBuilder.create;
import static org.testcontainers.utility.DockerImageName.parse;

@TestConfiguration
@Profile("test_standalone")
public class TestContainersConfig {

    private static final String ENV_TZ = "TZ";
    private static final String ASIA_SEOUL = "Asia/Seoul";

    private static final DockerImageName REDIS_IMAGE = parse("redis:7.2.4");
    private static final DockerImageName POSTGRES_IMAGE = parse("postgres:16.2");
    private static final DockerImageName MONGODB_IMAGE = parse("mongo:7.0.4");
    private static final DockerImageName KAFKA_IMAGE = parse("confluentinc/cp-kafka:7.6.0");

    @Container
    static GenericContainer<?> REDIS_CONTAINER =
            new GenericContainer<>(REDIS_IMAGE)
                    .withExposedPorts(6379)
                    .waitingFor(Wait.forLogMessage(".*Ready to accept connections.*\\n", 1))
                    .withReuse(true);

    @Container
    static PostgreSQLContainer<?> POSTGRES_CONTAINER =
            new PostgreSQLContainer<>(POSTGRES_IMAGE)
                    .withEnv(ENV_TZ, ASIA_SEOUL)
                    .withEnv("POSTGRES_DB", "test")
                    .withEnv("POSTGRES_USER", "test")
                    .withEnv("POSTGRES_PASSWORD", "test")
                    .withReuse(true);

    @Container
    static KafkaContainer KAFKA_CONTAINER =
            new KafkaContainer(KAFKA_IMAGE)
                    .withExposedPorts(9092, 9093)
                    .withReuse(true);

    static {
        POSTGRES_CONTAINER.start();
        REDIS_CONTAINER.start();

        System.setProperty("spring.data.redis.host", REDIS_CONTAINER.getHost());
        System.setProperty("spring.data.redis.port", REDIS_CONTAINER.getMappedPort(6379).toString());
    }

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", () -> POSTGRES_CONTAINER.getJdbcUrl());
        registry.add("spring.datasource.username", () -> POSTGRES_CONTAINER.getUsername());
        registry.add("spring.datasource.password", () -> POSTGRES_CONTAINER.getPassword());

        registry.add("spring.kafka.bootstrap-servers", () -> KAFKA_CONTAINER.getBootstrapServers());
        registry.add("spring.kafka.consumer.auto-offset-reset", () -> "earliest");
    }

    @Bean
    public DataSource dataSource() {
        return create()
                .url(POSTGRES_CONTAINER.getJdbcUrl())
                .username(POSTGRES_CONTAINER.getUsername())
                .password(POSTGRES_CONTAINER.getPassword())
                .build();
    }

}
