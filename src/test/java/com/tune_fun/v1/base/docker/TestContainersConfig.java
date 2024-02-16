package com.tune_fun.v1.base.docker;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.*;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

import javax.sql.DataSource;

import java.io.IOException;

import static org.springframework.boot.jdbc.DataSourceBuilder.create;
import static org.testcontainers.utility.DockerImageName.parse;

@TestConfiguration(proxyBeanMethods = false)
@Profile("test_standalone")
public class TestContainersConfig {

    private static final String ENV_TZ = "TZ";
    private static final String ASIA_SEOUL = "Asia/Seoul";

    private static final DockerImageName MARIADB_IMAGE = parse("mariadb:latest");
    /**
     * @see <a href="https://www.testcontainers.org/modules/databases/mariadb/">MariaDBContainer</a>
     */
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

    @Bean
    @DependsOn("mariaDBContainer")
    public static DataSource dataSource(MariaDBContainer<?> mariaDBContainer) {
        return create()
                .url(mariaDBContainer.getJdbcUrl())
                .username(mariaDBContainer.getUsername())
                .password(mariaDBContainer.getPassword())
                .build();
    }

//    private static final ComposeContainer DOCKER_COMPOSE_CONTAINER;
//
//    static {
//        try {
//            DOCKER_COMPOSE_CONTAINER = new ComposeContainer(new ClassPathResource("compose-test.yaml").getFile())
//                    .withLocalCompose(true)
//                    .withExposedService("redis", 6378, Wait.forListeningPort())
//                    .waitingFor("redis", Wait.forLogMessage("started", 1));
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
    private static final DockerImageName MONGODB_IMAGE = parse("mongo:latest");

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

    /**
     * @see <a href="https://www.testcontainers.org/modules/kafka/">KafkaContainer</a>
     */
    @Bean(initMethod = "start", destroyMethod = "stop")
    @ServiceConnection
    public static KafkaContainer kafkaContainer() {
        return new KafkaContainer(KAFKA_IMAGE)
                .withExposedPorts(9092, 9093);
    }
//
//    @DynamicPropertySource
//    @DependsOn("dockerComposeContainer")
//    public static void properties(DynamicPropertyRegistry registry) {
//        registry.add("spring.data.redis.host", () -> DOCKER_COMPOSE_CONTAINER.getServiceHost("redis", 6378));
//        registry.add("spring.data.redis.port", () -> DOCKER_COMPOSE_CONTAINER.getServicePort("redis", 6378));
//        registry.add("spring.data.redis.database", () -> 0);
//    }

}
