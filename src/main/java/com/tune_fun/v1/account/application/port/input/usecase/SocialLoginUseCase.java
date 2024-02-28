package com.tune_fun.v1.account.application.port.input.usecase;


import com.tune_fun.v1.account.application.port.input.command.AccountCommands;

@FunctionalInterface
public interface SocialLoginUseCase {

    void googleLogin(final AccountCommands.GoogleLogin command);

}
