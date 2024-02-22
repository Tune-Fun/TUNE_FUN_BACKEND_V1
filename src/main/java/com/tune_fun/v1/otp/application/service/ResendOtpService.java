package com.tune_fun.v1.otp.application.service;

import com.tune_fun.v1.common.hexagon.UseCase;
import com.tune_fun.v1.otp.application.port.input.command.OtpCommands;
import com.tune_fun.v1.otp.application.port.input.usecase.ResendOtpUseCase;
import com.tune_fun.v1.otp.application.port.output.SendOtpPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@UseCase
@RequiredArgsConstructor
public class ResendOtpService implements ResendOtpUseCase {

    private final SendOtpPort sendOtpPort;

    @Override
    public void resend(final OtpCommands.Resend command) {

    }
}
