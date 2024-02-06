package com.habin.demo.account.application.port.output.jwt;

public interface DeleteRefreshTokenPort {
    void deleteRefreshToken(final String accessToken);
}
