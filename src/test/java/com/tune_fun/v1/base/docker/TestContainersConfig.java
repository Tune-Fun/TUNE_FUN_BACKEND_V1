package com.tune_fun.v1.base.docker;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.utility.DockerImageName;

import javax.sql.DataSource;

import static org.testcontainers.utility.DockerImageName.parse;

@TestConfiguration(proxyBeanMethods = false)
@Profile("test_standalone")
public class TestContainersConfig {

    private static final String ENV_TZ = "TZ";
    private static final String ASIA_SEOUL = "Asia/Seoul";

    private static final DockerImageName MARIADB_IMAGE = parse("mariadb:latest");

    @Bean
    @DependsOn("mariaDBContainer")
    public static DataSource dataSource(MariaDBContainer<?> mariaDBContainer) {
        return DataSourceBuilder.create()
                .url(mariaDBContainer.getJdbcUrl())
                .username(mariaDBContainer.getUsername())
                .password(mariaDBContainer.getPassword())
                .build();
    }

    @Bean(initMethod = "start", destroyMethod = "stop")
    @ServiceConnection
    public static MariaDBContainer<?> mariaDBContainer() {
        return new MariaDBContainer<>(MARIADB_IMAGE)
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
    }

    private static final DockerImageName MONGODB_IMAGE = parse("mongo:6.0.9");

    /**
     * @see <a href="https://www.testcontainers.org/modules/databases/mongodb/">MongoDBContainer</a>
     * @see <a href="https://devs0n.tistory.com/48">Start Spring Data MongoDB - 5. Integration Test</a>
     */

    @Bean(initMethod = "start", destroyMethod = "stop")
    @ServiceConnection
    public static MongoDBContainer mongoDBContainer() {
        return new MongoDBContainer(MONGODB_IMAGE)
                .withEnv(ENV_TZ, ASIA_SEOUL)
                .withEnv("MONGO_INITDB_ROOT_USERNAME", "test")
                .withEnv("MONGO_INITDB_ROOT_PASSWORD", "test")
                .withSharding()
                .withExposedPorts(27017);
    }

    private static final DockerImageName KAFKA_IMAGE = parse("confluentinc/cp-kafka:latest");

    @Bean(initMethod = "start", destroyMethod = "stop")
    public static KafkaContainer kafkaContainer() {
        return new KafkaContainer(KAFKA_IMAGE);
    }

    @DynamicPropertySource
    public static void properties(DynamicPropertyRegistry registry,
                                  MongoDBContainer mongoDBContainer,
                                  KafkaContainer kafkaContainer
    ) {

//        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
//        registry.add("spring.data.mongodb.username", () -> "test");
//        registry.add("spring.data.mongodb.password", () -> "test");


        kafkaContainer.start();
        registry.add("spring.kafka.bootstrap-servers", kafkaContainer::getBootstrapServers);
        registry.add("spring.kafka.consumer.auto-offset-reset", () -> "earliest");
        registry.add("spring.kafka.consumer.group-id", () -> "test");
        registry.add("spring.kafka.consumer.key-deserializer", () -> "org.apache.kafka.common.serialization.StringDeserializer");
        registry.add("spring.kafka.consumer.value-deserializer", () -> "org.apache.kafka.common.serialization.StringDeserializer");
        registry.add("spring.kafka.producer.key-serializer", () -> "org.apache.kafka.common.serialization.StringSerializer");
        registry.add("spring.kafka.producer.value-serializer", () -> "org.apache.kafka.common.serialization.StringSerializer");
    }

}
