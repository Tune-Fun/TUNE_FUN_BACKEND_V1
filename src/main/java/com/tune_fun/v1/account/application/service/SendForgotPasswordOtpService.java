package com.tune_fun.v1.account.application.service;

import com.tune_fun.v1.account.application.port.input.usecase.SendForgotPasswordOtpUseCase;
import com.tune_fun.v1.account.application.port.output.LoadAccountPort;
import com.tune_fun.v1.common.hexagon.UseCase;
import com.tune_fun.v1.otp.application.port.output.SaveOtpPort;
import com.tune_fun.v1.otp.application.port.output.SendOtpPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@UseCase
@RequiredArgsConstructor
public class SendForgotPasswordOtpService implements SendForgotPasswordOtpUseCase {

    private final LoadAccountPort loadAccountPort;
    private final SaveOtpPort saveOtpPort;
    private final SendOtpPort sendOtpPort;

}
