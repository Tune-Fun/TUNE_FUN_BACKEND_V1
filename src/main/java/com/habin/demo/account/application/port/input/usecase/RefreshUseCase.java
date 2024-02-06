package com.habin.demo.account.application.port.input.usecase;

import com.habin.demo.account.application.port.input.command.AccountCommands;

@FunctionalInterface
public interface RefreshUseCase {
    String refresh(final AccountCommands.Refresh command);
}
