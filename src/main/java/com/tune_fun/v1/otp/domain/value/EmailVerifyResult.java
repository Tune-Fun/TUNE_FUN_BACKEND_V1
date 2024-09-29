package com.tune_fun.v1.otp.domain.value;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EmailVerifyResult implements VerifyResult {

    private String accessToken;
    
    private String refreshToken;
}
