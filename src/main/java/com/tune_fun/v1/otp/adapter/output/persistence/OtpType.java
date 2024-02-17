package com.tune_fun.v1.otp.adapter.output.persistence;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OtpType {

    FORGOT_PASSWORD("forgot-password");

    private final String label;

    public static OtpType fromLabel(String label) {
        for (OtpType type : OtpType.values()) if (type.getLabel().equals(label)) return type;
        return null;
    }

}
