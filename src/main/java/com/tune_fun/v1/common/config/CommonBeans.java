package com.tune_fun.v1.common.config;

import com.tune_fun.v1.account.application.port.output.oauth2.RevokeAppleOAuth2Port;
import com.tune_fun.v1.account.application.port.output.oauth2.RevokeGoogleOAuth2Port;
import com.tune_fun.v1.external.http.RetrofitClient;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.zalando.logbook.HttpRequest;
import org.zalando.logbook.Logbook;
import org.zalando.logbook.core.Conditions;
import org.zalando.logbook.json.PrettyPrintingJsonBodyFilter;

import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

import static com.tune_fun.v1.common.config.Uris.NOT_LOGGING_URIS;
import static java.util.Arrays.stream;
import static org.zalando.logbook.core.Conditions.exclude;

@Lazy
@Slf4j
@Configuration
public class CommonBeans {

    @Value("${spring.mail.host}")
    private String mailSenderHost;

    @Value("${spring.mail.port}")
    private int mailSenderPort;

    @Value("${spring.mail.username}")
    private String mailUsername;

    @Value("${spring.mail.password}")
    private String mailPassword;

    @Value("${spring.mail.properties.mail.debug}")
    private boolean debug;

    @Value("${spring.mail.properties.mail.smtp.auth}")
    private boolean auth;

    @Value("${spring.mail.properties.mail.smtp.timeout}")
    private int timeout;

    @Value("${spring.mail.properties.mail.smtp.starttls.enable}")
    private boolean starttls;

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost(mailSenderHost);
        javaMailSender.setPort(mailSenderPort);
        javaMailSender.setUsername(mailUsername);
        javaMailSender.setPassword(mailPassword);

        Properties properties = new Properties();
        properties.put("mail.debug", debug);
        properties.put("mail.smtp.auth", auth);
        properties.put("mail.smtp.timeout", timeout);
        properties.put("mail.smtp.starttls.enable", starttls);
        javaMailSender.setJavaMailProperties(properties);

        return javaMailSender;
    }

    @Bean
    @Qualifier("message")
    public MessageSource messageSource() {
        String basename = "messages/messages";
        String charSet = "UTF-8";
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename(basename);
        messageSource.setDefaultEncoding(charSet);
        return messageSource;
    }

    @Bean
    @Qualifier("validation")
    public MessageSource validationMessageSource() {
        String basename = "messages/validation";
        String charSet = "UTF-8";
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename(basename);
        messageSource.setDefaultEncoding(charSet);

        return messageSource;
    }

    @Bean
    public LocalValidatorFactoryBean getValidator(@Qualifier("validation") MessageSource messageSource) {
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSource);
        return bean;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public Logbook logbook() {
        Predicate<HttpRequest> excludePredicate = exclude(stream(NOT_LOGGING_URIS).map(Conditions::requestTo));

        return Logbook.builder()
                .bodyFilter(new PrettyPrintingJsonBodyFilter())
                .condition(excludePredicate)
                .build();
    }

    @Bean
    public RevokeGoogleOAuth2Port revokeGoogleOAuth2Port() {
        return RetrofitClient.getGoogleInstance().create(RevokeGoogleOAuth2Port.class);
    }

    @Bean
    public RevokeAppleOAuth2Port revokeAppleOAuth2Port() {
        return RetrofitClient.getAppleInstance().create(RevokeAppleOAuth2Port.class);
    }

}
