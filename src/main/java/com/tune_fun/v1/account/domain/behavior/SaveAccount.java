package com.tune_fun.v1.account.domain.behavior;

import com.tune_fun.v1.account.domain.value.Role;

public record SaveAccount(
        Role role,
        String uuid,
        String username,
        String password,
        String email,
        String nickname,
        Boolean voteProgressNotification,
        Boolean voteEndNotification,
        Boolean voteDeliveryNotification

) {
}
