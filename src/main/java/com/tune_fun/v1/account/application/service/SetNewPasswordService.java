package com.tune_fun.v1.account.application.service;

import com.tune_fun.v1.account.application.port.input.command.AccountCommands;
import com.tune_fun.v1.account.application.port.input.usecase.SetNewPasswordUseCase;
import com.tune_fun.v1.account.application.port.output.UpdatePasswordPort;
import com.tune_fun.v1.common.hexagon.UseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@UseCase
@RequiredArgsConstructor
public class SetNewPasswordService implements SetNewPasswordUseCase {

    private final UpdatePasswordPort updatePasswordPort;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void setNewPassword(final AccountCommands.SetNewPassword command, final User user) {
        String encodedNewPassword = passwordEncoder.encode(command.newPassword());
        updatePasswordPort.updatePassword(user.getUsername(), encodedNewPassword);
    }

}
