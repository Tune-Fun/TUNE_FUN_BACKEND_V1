package com.tune_fun.v1.common.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.zalando.logbook.HttpRequest;
import org.zalando.logbook.Logbook;
import org.zalando.logbook.core.Conditions;
import org.zalando.logbook.json.PrettyPrintingJsonBodyFilter;

import java.util.Arrays;
import java.util.function.Predicate;

import static org.zalando.logbook.core.Conditions.exclude;

@Configuration
public class CommonBeans {

    @Bean
    public MessageSource messageSource() {
        String basename = "messages/messages";
        String charSet = "UTF-8";
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename(basename);
        messageSource.setDefaultEncoding(charSet);
        return messageSource;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public Logbook logbook() {
        Predicate<HttpRequest> excludePredicate = exclude(Arrays.stream(Uris.NOT_LOGGING_URIS).map(Conditions::requestTo));

        return Logbook.builder()
                .bodyFilter(new PrettyPrintingJsonBodyFilter())
                .condition(excludePredicate)
                .build();
    }

}
