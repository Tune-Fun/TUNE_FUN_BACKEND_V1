package com.tune_fun.v1.base.annotation;

import com.tune_fun.v1.TuneFunV1Application;
import com.tune_fun.v1.base.integration.*;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;


// TODO : GitHub Actions 상에서 Test 종료시 Connection Error 확인 필요
@Target(TYPE)
@Retention(RUNTIME)
@SpringBootTest(
        classes = {TuneFunV1Application.class,
                TestContainersConfig.class,
                LocalStackConfig.class, ElasticMQConfig.class, MailConfig.class},
        properties = {"spring.main.allow-bean-definition-overriding=true"},
        webEnvironment = RANDOM_PORT
)
@ContextConfiguration(initializers = RandomPortInitializer.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test_standalone")
public @interface IntegrationTest {
}
