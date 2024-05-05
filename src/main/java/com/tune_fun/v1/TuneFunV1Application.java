package com.tune_fun.v1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication(exclude = {MongoAutoConfiguration.class})
@EnableFeignClients
@EnableJpaAuditing
public class TuneFunV1Application {

    public static void main(String[] args) {
        SpringApplication.run(TuneFunV1Application.class, args);
    }

}
