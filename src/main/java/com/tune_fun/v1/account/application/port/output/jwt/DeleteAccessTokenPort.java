package com.tune_fun.v1.account.application.port.output.jwt;

public interface DeleteAccessTokenPort {
    void deleteAccessToken(String accessToken);
}
