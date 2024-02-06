package com.habin.demo.account.application.port.output.jwt;

public interface CheckAccessTokenExpirePort {
    Boolean isAccessTokenExpired(final String accessToken);
}
