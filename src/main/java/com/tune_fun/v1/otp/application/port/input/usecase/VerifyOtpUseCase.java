package com.tune_fun.v1.otp.application.port.input.usecase;

import com.tune_fun.v1.otp.application.port.input.query.OtpQueries;

public interface VerifyOtpUseCase {
    void verify(final OtpQueries.Verify query) throws Exception;
}
