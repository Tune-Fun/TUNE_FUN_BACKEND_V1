package com.tune_fun.v1.account.application.port.input.usecase;

import com.tune_fun.v1.account.application.port.input.command.AccountCommands;
import org.springframework.security.core.userdetails.User;

public interface SetNewPasswordUseCase {
    void setNewPassword(final AccountCommands.SetNewPassword command, final User user);
}
