package com.tune_fun.v1.base;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.tune_fun.v1.TuneFunV1Application;
import com.tune_fun.v1.base.aws.LocalStackConfig;
import com.tune_fun.v1.base.docker.TestContainersConfig;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

import static org.testcontainers.utility.DockerImageName.parse;

@SpringBootTest(
        classes = TuneFunV1Application.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = "spring.main.allow-bean-definition-overriding=true"
)
@Import({TestContainersConfig.class, LocalStackConfig.class})
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
    private static final DockerImageName REDIS_IMAGE = parse("redis:latest");
    @Container
    public static final GenericContainer<?> REDIS_CONTAINER =
            new GenericContainer<>(REDIS_IMAGE)
                    .withEnv(ENV_TZ, ASIA_SEOUL)
                    .withExposedPorts(6379);


    @DynamicPropertySource
    public static void properties(DynamicPropertyRegistry registry) {
        REDIS_CONTAINER.start();
        registry.add("spring.data.redis.host", REDIS_CONTAINER::getHost);
        registry.add("spring.data.redis.port", () -> REDIS_CONTAINER.getMappedPort(6379).toString());
        registry.add("spring.data.redis.database", () -> 0);
    }

}
