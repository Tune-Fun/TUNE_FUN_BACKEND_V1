package com.tune_fun.v1.account.application.service.email;

import com.tune_fun.v1.account.application.port.input.command.AccountCommands;
import com.tune_fun.v1.account.application.port.input.usecase.email.RegisterEmailUseCase;
import com.tune_fun.v1.account.application.port.output.LoadAccountPort;
import com.tune_fun.v1.account.application.port.output.SaveEmailPort;
import com.tune_fun.v1.account.domain.value.CurrentAccount;
import com.tune_fun.v1.common.exception.CommonApplicationException;
import com.tune_fun.v1.common.stereotype.UseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@UseCase
@RequiredArgsConstructor
public class RegisterEmailService implements RegisterEmailUseCase {

    private final LoadAccountPort loadAccountPort;
    private final SaveEmailPort saveEmailPort;


    @Transactional
    @Override
    public void registerEmail(AccountCommands.SaveEmail command, User user) throws Exception {
        CurrentAccount currentAccount = loadAccountPort.currentAccountInfo(user.getUsername())
                .orElseThrow(CommonApplicationException.ACCOUNT_NOT_FOUND);

        if (currentAccount.email() != null)
            throw CommonApplicationException.USER_POLICY_CANNOT_REGISTER_EMAIL_TWICE;

        saveEmailPort.saveEmail(command.email(), user.getUsername());
    }

}
