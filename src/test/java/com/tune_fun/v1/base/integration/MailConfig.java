package com.tune_fun.v1.base.integration;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import static com.icegreen.greenmail.util.ServerSetup.PROTOCOL_SMTP;

@TestConfiguration
@Profile("test_standalone")
public class MailConfig {

    public static final String SMTP_USERNAME = "habin";
    public static final String SMTP_PASSWORD = "qpalzm0915()";

    @Value("${spring.mail.port}")
    private int smtpPort;

    @Bean(initMethod = "start", destroyMethod = "stop")
    public GreenMail greenMail() {
        ServerSetup smtpServerSetup = new ServerSetup(smtpPort, null, PROTOCOL_SMTP);
        GreenMail greenMail = new GreenMail(smtpServerSetup);
        greenMail.setUser(SMTP_USERNAME, SMTP_PASSWORD);
        return greenMail;
    }

}
