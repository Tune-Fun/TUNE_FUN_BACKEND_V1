package com.tune_fun.v1.account.application.port.output.jwt;

public interface CheckAccessTokenExpirePort {
    Boolean isAccessTokenExpired(final String accessToken);
}
