package com.tune_fun.v1.account.domain.behavior;

public record DeleteDevice(
        String username,
        String fcmToken,
        String deviceToken
) {
}
