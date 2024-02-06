package com.habin.demo.account.application.port.input.usecase.jwt;

@FunctionalInterface
public interface ValidateAccessTokenUseCase {

    Boolean validateAccessToken(String accessToken);

}
