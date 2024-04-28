package com.tune_fun.v1.account.application.port.input.usecase;

import com.tune_fun.v1.account.application.port.input.command.AccountCommands;
import com.tune_fun.v1.account.domain.value.RegisterResult;
import com.tune_fun.v1.vote.adapter.input.rest.RegisterType;

@FunctionalInterface
public interface RegisterUseCase {
    RegisterResult register(RegisterType type, final AccountCommands.Register command);
}
