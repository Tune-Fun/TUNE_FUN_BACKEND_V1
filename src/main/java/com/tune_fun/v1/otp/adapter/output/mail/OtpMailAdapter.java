package com.tune_fun.v1.otp.adapter.output.mail;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tune_fun.v1.common.util.EncryptUtil;
import com.tune_fun.v1.external.mail.MailHandler;
import com.tune_fun.v1.otp.application.port.output.SendOtpPort;
import com.tune_fun.v1.otp.domain.behavior.SendOtp;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;


@Component
@RequiredArgsConstructor
public class OtpMailAdapter implements SendOtpPort {
    private static final String OTP_SEND_MAIL_TEMPLATE = "otp_send";

    private final MailHandler mailHandler;
    private final EncryptUtil encryptUtil;
    private final ObjectMapper objectMapper;

    private static String getSubject(String nickname) {
        return "TuneFun - " + nickname + "님의 인증번호입니다.";
    }

    @Override
    public void sendOtp(SendOtp sendOtp) throws Exception {
        Map<String, Object> marshalledVariable = getTemplateVariables(sendOtp);
        mailHandler.templateEmailSend(sendOtp.email(), getSubject(sendOtp.nickname()), OTP_SEND_MAIL_TEMPLATE, marshalledVariable);
    }

    private Map<String, Object> getTemplateVariables(SendOtp sendOtp) throws Exception {
        String decryptedToken = encryptUtil.decrypt(sendOtp.token());
        OtpSendMailVariables variables = new OtpSendMailVariables(decryptedToken);
        return objectMapper.readValue(objectMapper.writeValueAsString(variables), new TypeReference<>() {
        });
    }

    private record OtpSendMailVariables(String otp) {
    }
}
