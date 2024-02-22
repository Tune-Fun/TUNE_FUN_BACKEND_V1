package com.tune_fun.v1.account.adapter.output.mail;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tune_fun.v1.account.application.port.output.SendUsernamePort;
import com.tune_fun.v1.account.domain.behavior.SendUsername;
import com.tune_fun.v1.external.mail.MailHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class AccountMailAdapter implements SendUsernamePort  {

    private static final String USERNAME_SEND_MAIL_TEMPLATE = "username_send";

    private final MailHandler mailHandler;
    private final ObjectMapper objectMapper;

    @Override
    public void sendUsername(final SendUsername sendUsername) throws Exception {
        Map<String, Object> marshalledVariable = getTemplateVariables(sendUsername);
        mailHandler.templateEmailSend(sendUsername.email(), getSubject(), USERNAME_SEND_MAIL_TEMPLATE, marshalledVariable);
    }

    private Map<String, Object> getTemplateVariables(final SendUsername sendUsername) throws Exception {
        UsernameSendMailVariables variables = new UsernameSendMailVariables(sendUsername.username());
        return objectMapper.readValue(objectMapper.writeValueAsString(variables), new TypeReference<>() {
        });
    }

    private static String getSubject() {
        return "TuneFun - 아이디 찾기 결과입니다.";
    }

    private record UsernameSendMailVariables(String username) {}
}
