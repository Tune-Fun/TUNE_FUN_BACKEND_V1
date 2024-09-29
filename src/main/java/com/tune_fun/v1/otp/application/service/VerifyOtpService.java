package com.tune_fun.v1.otp.application.service;

import com.tune_fun.v1.common.exception.CommonApplicationException;
import com.tune_fun.v1.common.stereotype.UseCase;
import com.tune_fun.v1.otp.domain.behavior.OtpType;
import com.tune_fun.v1.otp.application.port.input.query.OtpQueries;
import com.tune_fun.v1.otp.application.port.input.usecase.VerifyOtpUseCase;
import com.tune_fun.v1.otp.application.service.verificaitonstrategy.OtpVerificationStrategy;
import com.tune_fun.v1.otp.domain.behavior.VerifyOtp;
import com.tune_fun.v1.otp.domain.value.VerifyResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@UseCase
@RequiredArgsConstructor
public class VerifyOtpService implements VerifyOtpUseCase {

    private final List<OtpVerificationStrategy> otpVerificationStrategies;

    private static VerifyOtp getVerifyOtpBehavior(final OtpQueries.Verify query) {
        return new VerifyOtp(query.username(), OtpType.fromLabel(query.otpType()), query.otp());
    }

    @Override
    @Transactional
    public VerifyResult verify(final OtpQueries.Verify query) throws Exception {
        VerifyOtp verifyOtpBehavior = getVerifyOtpBehavior(query);

        return otpVerificationStrategies.stream()
                .filter(otpVerificationStrategy -> otpVerificationStrategy.matchedOtpType(verifyOtpBehavior.otpType()))
                .findFirst()
                .orElseThrow(() -> CommonApplicationException.USER_POLICY_USERNAME_REGISTERED)
                .verifyOtp(verifyOtpBehavior);
    }
}
