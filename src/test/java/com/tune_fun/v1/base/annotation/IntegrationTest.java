package com.tune_fun.v1.base.annotation;

import com.tune_fun.v1.TuneFunV1Application;
import com.tune_fun.v1.base.integration.*;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@Target(TYPE)
@Retention(RUNTIME)
@SpringBootTest(
        classes = TuneFunV1Application.class,
        properties = {"spring.main.allow-bean-definition-overriding=true"},
        webEnvironment = RANDOM_PORT
)
@ContextConfiguration(initializers = RandomPortInitializer.class)
@Testcontainers
@TestInstance(PER_CLASS)
@Import({TestContainersConfig.class, LocalStackConfig.class, ElasticMQConfig.class, MailConfig.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test_standalone")
public @interface IntegrationTest {
}
