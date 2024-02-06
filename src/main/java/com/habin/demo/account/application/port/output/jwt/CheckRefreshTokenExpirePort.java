package com.habin.demo.account.application.port.output.jwt;

public interface CheckRefreshTokenExpirePort {
    Boolean isRefreshTokenExpired(final String refreshToken);
}
