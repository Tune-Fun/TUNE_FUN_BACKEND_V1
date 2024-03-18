package com.tune_fun.v1.base.annotation;

import com.tune_fun.v1.TuneFunV1Application;
import com.tune_fun.v1.base.integration.ElasticMQConfig;
import com.tune_fun.v1.base.integration.LocalStackConfig;
import com.tune_fun.v1.base.integration.TestContainersConfig;
import com.tune_fun.v1.base.integration.MailConfig;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(classes = TuneFunV1Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import({TestContainersConfig.class, LocalStackConfig.class, ElasticMQConfig.class, MailConfig.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test_standalone")
public @interface IntegrationTest {
}
