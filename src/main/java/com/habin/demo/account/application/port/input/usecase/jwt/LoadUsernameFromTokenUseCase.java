package com.habin.demo.account.application.port.input.usecase.jwt;

@FunctionalInterface
public interface LoadUsernameFromTokenUseCase {
    String loadUsernameFromToken(final String token);
}
