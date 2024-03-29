package com.tune_fun.v1.account.application.service;

import com.tune_fun.v1.account.application.port.input.usecase.CheckEmailVerifiedUseCase;
import com.tune_fun.v1.account.application.port.output.LoadAccountPort;
import com.tune_fun.v1.account.domain.state.CurrentAccount;
import com.tune_fun.v1.common.exception.CommonApplicationException;
import com.tune_fun.v1.common.hexagon.UseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.tune_fun.v1.common.response.MessageCode.ACCOUNT_NOT_FOUND;
import static com.tune_fun.v1.common.response.MessageCode.EXCEPTION_EMAIL_NOT_VERIFIED;


@Service
@UseCase
@RequiredArgsConstructor
public class CheckEmailVerifiedService implements CheckEmailVerifiedUseCase {

    private final LoadAccountPort loadAccountPort;

    @Override
    @Transactional
    public void checkEmailVerified(final User user) {
        CurrentAccount currentAccount = loadAccountPort.currentAccountInfo(user.getUsername())
                .orElseThrow(() -> new CommonApplicationException(ACCOUNT_NOT_FOUND));

        if (currentAccount.emailVerifiedAt() == null)
            throw new CommonApplicationException(EXCEPTION_EMAIL_NOT_VERIFIED);
    }
}
