package com.tune_fun.v1.external.mail;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
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

    public String renderTemplateText(String template, Map<String, Object> variables) {
        Context context = new Context(Locale.getDefault());
        context.setVariables(variables);
        return templateEngine.process(template, context);
    }

    @Async("emailAsync")
    public void emailSend(final String target, final String from, final String subject, final String text) throws Exception {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, UTF_8.name());

        messageHelper.setTo(target);
        messageHelper.setFrom(from);
        messageHelper.setSubject(subject);
        messageHelper.setText(text);
        javaMailSender.send(message);
    }

}
