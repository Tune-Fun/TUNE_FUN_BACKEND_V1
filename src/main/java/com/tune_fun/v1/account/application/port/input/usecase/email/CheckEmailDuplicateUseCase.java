package com.tune_fun.v1.account.application.port.input.usecase.email;

@FunctionalInterface
public interface CheckEmailDuplicateUseCase {
    void checkEmailDuplicate(final String email);
}
