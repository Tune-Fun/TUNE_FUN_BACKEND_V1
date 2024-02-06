package com.tune_fun.v1.account.application.port.output.jwt;

public interface CheckRefreshTokenExpirePort {
    Boolean isRefreshTokenExpired(final String refreshToken);
}
