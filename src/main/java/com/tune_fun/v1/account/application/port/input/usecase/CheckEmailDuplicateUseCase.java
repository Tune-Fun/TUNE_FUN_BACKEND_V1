package com.tune_fun.v1.account.application.port.input.usecase;

@FunctionalInterface
public interface CheckEmailDuplicateUseCase {
    void checkEmailDuplicate(final String email);
}
