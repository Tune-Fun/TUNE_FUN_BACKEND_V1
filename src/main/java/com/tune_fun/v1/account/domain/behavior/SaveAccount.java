package com.tune_fun.v1.account.domain.behavior;

public record SaveAccount(
        String uuid,
        String username,
        String password,
        String email,
        String nickname

) {
}
