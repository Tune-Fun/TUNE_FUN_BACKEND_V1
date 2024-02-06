package com.habin.demo.account.application.port.output.jwt;

public interface ReissueAccessTokenPort {
    String reissueAccessToken(String refreshTokenValue);
}
