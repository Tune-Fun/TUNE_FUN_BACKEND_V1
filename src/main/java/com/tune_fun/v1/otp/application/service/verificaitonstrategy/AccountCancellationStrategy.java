package com.tune_fun.v1.otp.application.service.verificaitonstrategy;

import com.tune_fun.v1.otp.domain.behavior.OtpType;
import com.tune_fun.v1.otp.application.port.output.VerifyOtpPort;
import com.tune_fun.v1.otp.domain.behavior.VerifyOtp;
import com.tune_fun.v1.otp.domain.value.AccountCancellationVerificationResult;
import com.tune_fun.v1.otp.domain.value.VerifyResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountCancellationStrategy extends BaseOtpVerificationStrategy {

    private final VerifyOtpPort verifyOtpPort;

    @Override
    public VerifyResult verifyOtp(VerifyOtp verifyOtp) throws Exception {
        verifyOtpPort.verifyOtp(verifyOtp);
        return new AccountCancellationVerificationResult();
    }

    @Override
    public boolean matchedOtpType(OtpType otpType) {
        return otpType == OtpType.ACCOUNT_CANCELLATION;
    }
}
