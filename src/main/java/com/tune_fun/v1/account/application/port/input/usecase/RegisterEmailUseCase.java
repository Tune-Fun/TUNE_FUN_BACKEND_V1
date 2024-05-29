package com.tune_fun.v1.account.application.port.input.usecase;

import com.tune_fun.v1.account.application.port.input.command.AccountCommands;
import org.springframework.security.core.userdetails.User;

@FunctionalInterface
public interface RegisterEmailUseCase {

    void registerEmail(final AccountCommands.Register command, final User user) throws Exception;

}
