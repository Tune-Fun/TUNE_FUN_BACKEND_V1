package com.tune_fun.v1.account.application.port.input.usecase;

public interface FindUsernameUseCase {
    void findUsername(final String email) throws Exception;
}
