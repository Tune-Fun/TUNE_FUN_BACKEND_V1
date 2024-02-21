package com.tune_fun.v1.otp.application.service;

import com.tune_fun.v1.common.hexagon.UseCase;
import com.tune_fun.v1.otp.application.port.input.usecase.VerifyOtpUseCase;
import com.tune_fun.v1.otp.application.port.output.VerifyOtpPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@UseCase
@RequiredArgsConstructor
public class VerifyOtpService implements VerifyOtpUseCase {

    private final VerifyOtpPort verifyOtpPort;

}
