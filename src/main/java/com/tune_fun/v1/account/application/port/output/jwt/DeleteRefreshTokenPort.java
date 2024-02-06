package com.tune_fun.v1.account.application.port.output.jwt;

public interface DeleteRefreshTokenPort {
    void deleteRefreshToken(final String accessToken);
}
