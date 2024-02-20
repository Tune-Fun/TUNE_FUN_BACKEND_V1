package com.tune_fun.v1.otp.application.port.output;

import com.tune_fun.v1.otp.domain.behavior.LoadOtp;
import com.tune_fun.v1.otp.domain.state.CurrentDecryptedOtp;

public interface LoadOtpPort {
    CurrentDecryptedOtp loadOtp(final LoadOtp loadOtp) throws Exception;
}
