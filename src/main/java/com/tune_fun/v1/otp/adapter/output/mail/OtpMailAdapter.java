package com.tune_fun.v1.otp.adapter.output.mail;

import com.tune_fun.v1.external.mail.MailHandler;
import com.tune_fun.v1.otp.application.port.output.SendOtpPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OtpMailAdapter implements SendOtpPort {

    private final MailHandler mailHandler;

}
