package com.tune_fun.v1.common.config;

import com.tune_fun.v1.common.config.annotation.OnlyDevelopmentConfiguration;
import feign.Logger;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.cloud.openfeign.clientconfig.Http2ClientFeignConfiguration;
import org.springframework.context.annotation.Bean;

@OnlyDevelopmentConfiguration
@ImportAutoConfiguration({FeignAutoConfiguration.class, Http2ClientFeignConfiguration.class})
public class OpenFeignConfig {

    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

}
