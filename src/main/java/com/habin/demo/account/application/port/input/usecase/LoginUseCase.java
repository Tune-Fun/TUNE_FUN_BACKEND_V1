package com.habin.demo.account.application.port.input.usecase;

import com.habin.demo.account.application.port.input.command.AccountCommands;
import com.habin.demo.account.domain.state.LoginResult;

@FunctionalInterface
public interface LoginUseCase {
    LoginResult login(AccountCommands.Login command);
}
