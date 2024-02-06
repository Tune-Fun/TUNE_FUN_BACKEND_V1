package com.tune_fun.v1.account.domain.behavior;

public record SaveJwtToken(
        String username,
        String authorities
) {
}
