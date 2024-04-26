package com.tune_fun.v1.account.domain.value;

public record NotificationApprovedDevice(
        String id,
        String username,
        String nickname,
        String fcmToken,
        String deviceToken
) {
}
