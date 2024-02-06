package com.tune_fun.v1.account.application.port.input.usecase;

@FunctionalInterface
public interface LogoutUseCase {
    void logout(final String accessToken);
}
