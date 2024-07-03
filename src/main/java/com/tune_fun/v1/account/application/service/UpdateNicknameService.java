package com.tune_fun.v1.account.application.service;

import com.tune_fun.v1.account.application.port.input.command.AccountCommands;
import com.tune_fun.v1.account.application.port.input.usecase.UpdateNicknameUseCase;
import com.tune_fun.v1.account.application.port.output.LoadAccountPort;
import com.tune_fun.v1.account.application.port.output.UpdateNicknamePort;
import com.tune_fun.v1.common.exception.CommonApplicationException;
import com.tune_fun.v1.common.stereotype.UseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@UseCase
@RequiredArgsConstructor
public class UpdateNicknameService implements UpdateNicknameUseCase {

    private final LoadAccountPort loadAccountPort;
    private final UpdateNicknamePort updateNicknamePort;

    @Override
    @Transactional
    public void updateNickname(final AccountCommands.UpdateNickname command, final User user) {
        if (loadAccountPort.registeredAccountInfoByNickname(command.newNickname()).isPresent())
            throw CommonApplicationException.USER_POLICY_NICKNAME_REGISTERED;

        updateNicknamePort.updateNickname(user.getUsername(), command.newNickname());
    }
}
