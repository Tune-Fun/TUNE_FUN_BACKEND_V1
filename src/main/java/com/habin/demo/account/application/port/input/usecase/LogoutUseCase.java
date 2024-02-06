package com.habin.demo.account.application.port.input.usecase;

@FunctionalInterface
public interface LogoutUseCase {
    void logout(final String accessToken);
}
