package com.tune_fun.v1.account.application.port.input.usecase;

@FunctionalInterface
public interface CheckUsernameDuplicateUseCase {
    void checkUsernameDuplicate(final String username);
}
