package com.tune_fun.v1.account.application.port.input.usecase.email;

import com.tune_fun.v1.account.application.port.input.command.AccountCommands;
import org.springframework.security.core.userdetails.User;

@FunctionalInterface
public interface ChangeEmailUseCase {
    void changeEmail(final AccountCommands.SaveEmail command, final User user);
}
