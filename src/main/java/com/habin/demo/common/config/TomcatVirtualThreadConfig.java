package com.habin.demo.common.config;

import org.springframework.boot.web.embedded.tomcat.TomcatProtocolHandlerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static java.util.concurrent.Executors.newVirtualThreadPerTaskExecutor;

@Configuration
public class TomcatVirtualThreadConfig {

    @Bean
    public TomcatProtocolHandlerCustomizer<?> protocolHandlerVirtualThreadExecutorCustomizer() {
        return protocolHandler -> protocolHandler.setExecutor(newVirtualThreadPerTaskExecutor());
    }

}
