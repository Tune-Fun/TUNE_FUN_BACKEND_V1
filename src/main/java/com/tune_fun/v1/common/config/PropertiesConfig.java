package com.tune_fun.v1.common.config;

import com.tune_fun.v1.common.property.EventProperty;
import com.tune_fun.v1.common.property.FcmProperty;
import com.tune_fun.v1.common.property.JwtProperty;
import com.tune_fun.v1.common.property.SpotifyProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@EnableConfigurationProperties({
        EventProperty.class,
        FcmProperty.class,
        JwtProperty.class,
        SpotifyProperty.class
})
@Configuration
public class PropertiesConfig {}
