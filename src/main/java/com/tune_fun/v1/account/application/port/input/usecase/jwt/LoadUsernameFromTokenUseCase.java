package com.tune_fun.v1.account.application.port.input.usecase.jwt;

@FunctionalInterface
public interface LoadUsernameFromTokenUseCase {
    String loadUsernameFromToken(final String token);
}
