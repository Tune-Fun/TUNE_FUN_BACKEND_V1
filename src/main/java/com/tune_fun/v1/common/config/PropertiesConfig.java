package com.tune_fun.v1.common.config;

import com.tune_fun.v1.common.property.*;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@EnableConfigurationProperties({
        AppleProperty.class,
        EventProperty.class,
        FcmProperty.class,
        JwtProperty.class,
        SpotifyProperty.class
})
@Configuration
public class PropertiesConfig {}
