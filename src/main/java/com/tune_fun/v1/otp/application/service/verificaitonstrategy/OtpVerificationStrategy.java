package com.tune_fun.v1.otp.application.service.verificaitonstrategy;

import com.tune_fun.v1.otp.domain.behavior.OtpType;
import com.tune_fun.v1.otp.domain.behavior.VerifyOtp;
import com.tune_fun.v1.otp.domain.value.VerifyResult;

public interface OtpVerificationStrategy {

    VerifyResult verifyOtp(VerifyOtp verifyOtp) throws Exception;

    boolean matchedOtpType(OtpType otpType);
}
