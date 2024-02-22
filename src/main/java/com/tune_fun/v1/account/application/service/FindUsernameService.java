package com.tune_fun.v1.account.application.service;

import com.tune_fun.v1.account.application.port.input.usecase.FindUsernameUseCase;
import com.tune_fun.v1.account.application.port.output.LoadAccountPort;
import com.tune_fun.v1.account.application.port.output.SendUsernamePort;
import com.tune_fun.v1.account.domain.behavior.SendUsername;
import com.tune_fun.v1.account.domain.state.RegisteredAccount;
import com.tune_fun.v1.common.exception.CommonApplicationException;
import com.tune_fun.v1.common.hexagon.UseCase;
import com.tune_fun.v1.common.response.MessageCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.tune_fun.v1.common.response.MessageCode.ACCOUNT_NOT_FOUND;

@Service
@UseCase
@RequiredArgsConstructor
public class FindUsernameService implements FindUsernameUseCase {

    private final LoadAccountPort loadAccountPort;
    private final SendUsernamePort sendUsernamePort;

    @Override
    @Transactional(readOnly = true)
    public void findUsername(final String email) throws Exception {
        Optional<RegisteredAccount> registeredAccount = loadAccountPort.registeredAccountInfoByEmail(email);

        if (registeredAccount.isPresent()) {
            SendUsername sendUsernameBehavior = new SendUsername(email, registeredAccount.get().username());
            sendUsernamePort.sendUsername(sendUsernameBehavior);
            return;
        }

        throw new CommonApplicationException(ACCOUNT_NOT_FOUND);
    }
}
