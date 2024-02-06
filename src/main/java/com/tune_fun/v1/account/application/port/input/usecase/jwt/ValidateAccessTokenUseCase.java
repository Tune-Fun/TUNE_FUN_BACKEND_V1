package com.tune_fun.v1.account.application.port.input.usecase.jwt;

@FunctionalInterface
public interface ValidateAccessTokenUseCase {

    Boolean validateAccessToken(String accessToken);

}
