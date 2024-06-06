package com.tune_fun.v1.account.application.service.email;

import com.tune_fun.v1.account.application.port.input.command.AccountCommands;
import com.tune_fun.v1.account.application.port.input.usecase.email.ChangeEmailUseCase;
import com.tune_fun.v1.account.application.port.output.RecordEmailVerifiedAtPort;
import com.tune_fun.v1.account.application.port.output.SaveEmailPort;
import com.tune_fun.v1.common.stereotype.UseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@UseCase
@RequiredArgsConstructor
public class ChangeEmailService implements ChangeEmailUseCase {

    private final SaveEmailPort saveEmailPort;
    private final RecordEmailVerifiedAtPort recordEmailVerifiedAtPort;

    @Transactional
    @Override
    public void changeEmail(final AccountCommands.SaveEmail command, final User user) {
        saveEmailPort.saveEmail(command.email(), user.getUsername());
        recordEmailVerifiedAtPort.clearEmailVerifiedAt(user.getUsername());
    }
}
