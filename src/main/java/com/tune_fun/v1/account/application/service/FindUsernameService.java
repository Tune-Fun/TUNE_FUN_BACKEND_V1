package com.tune_fun.v1.account.application.service;

import com.tune_fun.v1.account.application.port.input.query.AccountQueries;
import com.tune_fun.v1.account.application.port.input.usecase.FindUsernameUseCase;
import com.tune_fun.v1.account.application.port.output.LoadAccountPort;
import com.tune_fun.v1.account.application.port.output.SendUsernamePort;
import com.tune_fun.v1.account.domain.behavior.SendUsername;
import com.tune_fun.v1.account.domain.value.RegisteredAccount;
import com.tune_fun.v1.common.exception.CommonApplicationException;
import com.tune_fun.v1.common.hexagon.UseCase;
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
    public void findUsername(final AccountQueries.Username query) throws Exception {
        Optional<RegisteredAccount> registeredAccount = loadAccountPort.registeredAccountInfoByEmail(query.email());

        if (registeredAccount.isPresent()) {
            SendUsername sendUsernameBehavior = new SendUsername(query.email(), registeredAccount.get().username());
            sendUsernamePort.sendUsername(sendUsernameBehavior);
            return;
        }

        throw new CommonApplicationException(ACCOUNT_NOT_FOUND);
    }
}
