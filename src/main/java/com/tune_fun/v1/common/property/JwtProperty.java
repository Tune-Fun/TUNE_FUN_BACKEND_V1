package com.tune_fun.v1.common.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties("jwt")
public record JwtProperty(String secret, Duration accessTokenValidity, Duration refreshTokenValidity) {

    public Long getAccessTokenValidity() {
        return accessTokenValidity.toMillis();
    }

    public Long getRefreshTokenValidity() {
        return refreshTokenValidity.toMillis();
    }

}
