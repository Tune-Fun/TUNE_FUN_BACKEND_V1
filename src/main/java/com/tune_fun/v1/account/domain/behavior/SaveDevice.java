package com.tune_fun.v1.account.domain.behavior;

public record SaveDevice(
        String username,
        String fcmToken,
        String deviceToken
) {
}
