package com.tune_fun.v1.otp.application.port.input.usecase;

import com.tune_fun.v1.otp.application.port.input.command.OtpCommands;

public interface ResendOtpUseCase {
    void resend(final OtpCommands.Resend command) throws Exception;
}
