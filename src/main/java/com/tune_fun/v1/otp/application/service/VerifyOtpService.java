package com.tune_fun.v1.otp.application.service;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import com.tune_fun.v1.account.application.port.output.RecordEmailVerifiedAtPort;
import com.tune_fun.v1.common.hexagon.UseCase;
import com.tune_fun.v1.otp.application.port.input.query.OtpQueries;
import com.tune_fun.v1.otp.application.port.input.usecase.VerifyOtpUseCase;
import com.tune_fun.v1.otp.application.port.output.VerifyOtpPort;
import com.tune_fun.v1.otp.domain.behavior.VerifyOtp;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@XRayEnabled
@Service
@UseCase
@RequiredArgsConstructor
public class VerifyOtpService implements VerifyOtpUseCase {

    private final VerifyOtpPort verifyOtpPort;
    private final RecordEmailVerifiedAtPort recordEmailVerifiedAtPort;

    @Override
    public void verify(final OtpQueries.Verify query) throws Exception {
        VerifyOtp verifyOtpBehavior = getVerifyOtpBehavior(query);
        verifyOtpPort.verifyOtp(verifyOtpBehavior);
        recordEmailVerifiedAtPort.recordEmailVerifiedAt(query.username());
    }

    private static VerifyOtp getVerifyOtpBehavior(final OtpQueries.Verify query) {
        return new VerifyOtp(query.username(), query.otpType(), query.otp());
    }
}
