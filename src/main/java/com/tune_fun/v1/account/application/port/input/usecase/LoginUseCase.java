package com.tune_fun.v1.account.application.port.input.usecase;

import com.tune_fun.v1.account.application.port.input.command.AccountCommands;
import com.tune_fun.v1.account.domain.value.LoginResult;

@FunctionalInterface
public interface LoginUseCase {
    LoginResult login(AccountCommands.Login command);
}
