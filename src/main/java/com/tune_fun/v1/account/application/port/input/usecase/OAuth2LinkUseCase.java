package com.tune_fun.v1.account.application.port.input.usecase;

import com.tune_fun.v1.account.application.port.input.command.AccountCommands;
import org.springframework.security.core.userdetails.User;

public interface OAuth2LinkUseCase {
    void linkGoogle(final AccountCommands.OAuth2Link command, final User user);
}
