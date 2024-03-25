package com.tune_fun.v1.common.config;

import com.tune_fun.v1.common.property.AppleProperty;
import com.tune_fun.v1.common.property.EventProperty;
import com.tune_fun.v1.common.property.FcmProperty;
import com.tune_fun.v1.common.property.JwtProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@EnableConfigurationProperties({
        AppleProperty.class,
        EventProperty.class,
        FcmProperty.class,
        JwtProperty.class
})
@Configuration
public class PropertiesConfig {}
