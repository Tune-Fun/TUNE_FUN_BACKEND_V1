package com.tune_fun.v1.account.application.port.input.usecase;

@FunctionalInterface
public interface SendAccountCancellationOtpUseCase {

    void sendOtp(String username) throws Exception;
}
