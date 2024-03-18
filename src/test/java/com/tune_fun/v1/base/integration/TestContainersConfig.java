package com.tune_fun.v1.base.integration;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import javax.sql.DataSource;

import static org.springframework.boot.jdbc.DataSourceBuilder.create;
import static org.testcontainers.utility.DockerImageName.parse;

@TestConfiguration(proxyBeanMethods = false)
@Profile("test_standalone")
public class TestContainersConfig {

    private static final String ENV_TZ = "TZ";
    private static final String ASIA_SEOUL = "Asia/Seoul";

    private static final DockerImageName REDIS_IMAGE = parse("redis:latest");

    static {
        GenericContainer<?> REDIS_CONTAINER =
                new GenericContainer<>(REDIS_IMAGE)
                        .withExposedPorts(6379)
                        .withReuse(true);

        REDIS_CONTAINER.start();

        System.setProperty("spring.data.redis.host", REDIS_CONTAINER.getHost());
        System.setProperty("spring.data.redis.port", REDIS_CONTAINER.getMappedPort(6379).toString());
    }

    private static final DockerImageName POSTGRES_IMAGE = parse("postgres:latest");

    /**
     * @see <a href="https://java.testcontainers.org/modules/databases/postgres/">PostgresContainer</a>
     */
    @Bean(initMethod = "start", destroyMethod = "stop")
    @ServiceConnection
    public static PostgreSQLContainer<?> postgresContainer() {
        return new PostgreSQLContainer<>(POSTGRES_IMAGE)
                .withEnv(ENV_TZ, ASIA_SEOUL)
                .withEnv("POSTGRES_DB", "test")
                .withEnv("POSTGRES_USER", "test")
                .withEnv("POSTGRES_PASSWORD", "test");
    }

    @Bean
    @DependsOn("postgresContainer")
    public static DataSource dataSource(PostgreSQLContainer<?> postgresSQLContainer) {
        return create()
                .url(postgresSQLContainer.getJdbcUrl())
                .username(postgresSQLContainer.getUsername())
                .password(postgresSQLContainer.getPassword())
                .build();
    }

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

}
