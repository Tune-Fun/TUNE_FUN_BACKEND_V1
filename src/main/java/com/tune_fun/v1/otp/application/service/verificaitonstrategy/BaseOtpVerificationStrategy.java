package com.tune_fun.v1.otp.application.service.verificaitonstrategy;

import com.tune_fun.v1.account.domain.behavior.SaveJwtToken;
import com.tune_fun.v1.account.domain.value.CurrentAccount;
import com.tune_fun.v1.common.constant.Constants;
import com.tune_fun.v1.otp.domain.behavior.OtpType;
import com.tune_fun.v1.otp.domain.behavior.VerifyOtp;
import com.tune_fun.v1.otp.domain.value.VerifyResult;
import org.jetbrains.annotations.NotNull;

public abstract class BaseOtpVerificationStrategy implements OtpVerificationStrategy {

    @Override
    public abstract VerifyResult verifyOtp(VerifyOtp verifyOtp) throws Exception;

    @Override
    public abstract boolean matchedOtpType(OtpType otpType);

    @NotNull
    protected static SaveJwtToken getSaveJwtTokenBehavior(CurrentAccount currentAccount) {
        String authorities = String.join(Constants.COMMA, currentAccount.roles());
        return new SaveJwtToken(currentAccount.username(), authorities);
    }
}
