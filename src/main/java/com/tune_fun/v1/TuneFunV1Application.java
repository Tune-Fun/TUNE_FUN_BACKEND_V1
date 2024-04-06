package com.tune_fun.v1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class TuneFunV1Application {

    public static void main(String[] args) {
        SpringApplication.run(TuneFunV1Application.class, args);
    }

}
