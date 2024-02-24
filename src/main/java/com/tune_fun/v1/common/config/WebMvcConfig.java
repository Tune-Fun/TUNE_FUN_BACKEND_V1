package com.tune_fun.v1.common.config;

import com.amazonaws.xray.AWSXRay;
import com.amazonaws.xray.AWSXRayRecorderBuilder;
import com.amazonaws.xray.jakarta.servlet.AWSXRayServletFilter;
import com.amazonaws.xray.plugins.EC2Plugin;
import com.amazonaws.xray.strategy.LogErrorContextMissingStrategy;
import com.tune_fun.v1.common.config.annotation.OnlyDevelopmentConfiguration;
import jakarta.servlet.Filter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static com.amazonaws.xray.strategy.jakarta.SegmentNamingStrategy.dynamic;

@OnlyDevelopmentConfiguration
public class WebMvcConfig implements WebMvcConfigurer {

    static {
        AWSXRayRecorderBuilder builder = AWSXRayRecorderBuilder.standard()
                .withPlugin(new EC2Plugin());

        builder.withContextMissingStrategy(new LogErrorContextMissingStrategy());
        AWSXRay.setGlobalRecorder(builder.build());
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/css/**").addResourceLocations("classpath:/static/css");
        registry.addResourceHandler("/js/**").addResourceLocations("classpath:/static/js/");
        registry.addResourceHandler("/images/**").addResourceLocations("classpath:/static/images/");
    }

    @Bean
    public Filter TracingFilter() {
        return new AWSXRayServletFilter(dynamic("Scorekeep"));
    }

}
