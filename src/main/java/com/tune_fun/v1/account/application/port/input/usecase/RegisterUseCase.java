package com.tune_fun.v1.account.application.port.input.usecase;

import com.tune_fun.v1.account.application.port.input.command.AccountCommands;
import com.tune_fun.v1.account.domain.value.RegisterResult;
import com.tune_fun.v1.account.domain.value.Role;

@FunctionalInterface
public interface RegisterUseCase {
    RegisterResult register(Role role, final AccountCommands.Register command);
}
