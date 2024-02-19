package com.tune_fun.v1.base.mail;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import static com.icegreen.greenmail.util.ServerSetup.PROTOCOL_SMTP;

@TestConfiguration
public class MailConfig {

    public static final String SMTP_USERNAME = "habin";
    public static final String SMTP_PASSWORD = "qpalzm0915()";

    @Bean(initMethod = "start", destroyMethod = "stop")
    public GreenMail greenMail() {
        ServerSetup smtpServerSetup = new ServerSetup(3025, null, PROTOCOL_SMTP);
        GreenMail greenMail = new GreenMail(smtpServerSetup);
        greenMail.setUser(SMTP_USERNAME, SMTP_PASSWORD);
        return greenMail;
    }

}