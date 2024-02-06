package com.habin.demo.account.domain.behavior;

public record SaveJwtToken(
        String username,
        String authorities
) {
}
