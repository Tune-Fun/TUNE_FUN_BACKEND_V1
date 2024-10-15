package com.tune_fun.v1.otp.domain.behavior;

import com.tune_fun.v1.common.exception.CommonApplicationException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OtpType {

    FORGOT_PASSWORD("forgot-password"),
    VERIFY_EMAIL("verify-email"),
    CHANGE_EMAIL("change-email"),
    CHANGE_PASSWORD("change-password"),
    WITHDRAWAL("withdrawal"),
    ACCOUNT_CANCELLATION("account-cancellation");

    private final String label;

    public static OtpType fromLabel(String label) {
        for (OtpType type : OtpType.values()) if (type.getLabel().equals(label)) return type;
        throw CommonApplicationException.EXCEPTION_OTP_TYPE_NOT_FOUND;
    }

}
