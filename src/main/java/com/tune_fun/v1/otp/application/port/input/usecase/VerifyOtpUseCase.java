package com.tune_fun.v1.otp.application.port.input.usecase;

import com.tune_fun.v1.otp.application.port.input.query.OtpQueries;
import com.tune_fun.v1.otp.domain.value.VerifyResult;

@FunctionalInterface
public interface VerifyOtpUseCase {
    VerifyResult verify(final OtpQueries.Verify query) throws Exception;
}
