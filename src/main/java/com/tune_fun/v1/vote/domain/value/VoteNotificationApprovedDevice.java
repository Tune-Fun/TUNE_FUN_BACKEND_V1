package com.tune_fun.v1.vote.domain.value;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public final class VoteNotificationApprovedDevice {
    private Long id;
    private String username;
    private String nickname;
    private String fcmToken;
    private String deviceToken;
}
