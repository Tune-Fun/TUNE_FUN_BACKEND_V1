package com.tune_fun.v1.otp.adapter.output.mail;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tune_fun.v1.common.util.EncryptUtil;
import com.tune_fun.v1.external.aws.kms.KmsProvider;
import com.tune_fun.v1.external.mail.MailHandler;
import com.tune_fun.v1.otp.application.port.output.SendOtpPort;
import com.tune_fun.v1.otp.domain.behavior.SendOtp;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import static org.apache.tomcat.util.codec.binary.Base64.decodeBase64;


@Component
@RequiredArgsConstructor
public class OtpMailAdapter implements SendOtpPort {
    private static final String OTP_SEND_MAIL_TEMPLATE = "otp_send";

    private final MailHandler mailHandler;
    private final KmsProvider kmsProvider;
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
        String decryptedToken = getDecryptedToken(sendOtp.token());
        OtpSendMailVariables variables = new OtpSendMailVariables(decryptedToken);
        return objectMapper.readValue(objectMapper.writeValueAsString(variables), new TypeReference<>() {
        });
    }

    private record OtpSendMailVariables(String otp) {
    }

    private String getDecryptedToken(@NotBlank String token) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        int keyEnd = token.indexOf(':');
        byte[] encryptedKey = decodeBase64(token.substring(0, keyEnd));
        byte[] encryptedToken = decodeBase64(token.substring(keyEnd + 1));
        byte[] plainText = kmsProvider.requestPlaintextKey(encryptedKey).plaintext().asByteArray();

        return EncryptUtil.decrypt(plainText, encryptedToken);
    }
}
