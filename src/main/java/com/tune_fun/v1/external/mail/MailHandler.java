package com.tune_fun.v1.external.mail;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Locale;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;

@Component
@RequiredArgsConstructor
public class MailHandler {

    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String mailSenderId;

    public String renderTemplateText(String template, Map<String, Object> variables) {
        Context context = new Context(Locale.getDefault());
        context.setVariables(variables);
        return templateEngine.process(template, context);
    }

    public void templateEmailSend(final String target, final String subject, final String template,
                                  final Map<String, Object> variables) throws Exception {
        String text = renderTemplateText(template, variables);
        emailSend(target, subject, text);
    }

    @Async("emailAsync")
    public void emailSend(final String target, final String subject, final String text) throws Exception {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");

        messageHelper.setTo(target);
        messageHelper.setFrom(mailSenderId);
        messageHelper.setSubject(subject);
        messageHelper.setText(text, true);
        javaMailSender.send(message);
    }

}
