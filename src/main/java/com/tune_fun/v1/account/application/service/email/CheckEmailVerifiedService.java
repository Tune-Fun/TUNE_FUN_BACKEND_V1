package com.tune_fun.v1.account.application.service.email;

import com.tune_fun.v1.account.application.port.input.usecase.email.CheckEmailVerifiedUseCase;
import com.tune_fun.v1.account.application.port.output.LoadAccountPort;
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
public class CheckEmailVerifiedService implements CheckEmailVerifiedUseCase {

    private final LoadAccountPort loadAccountPort;

    @Override
    @Transactional
    public void checkEmailVerified(final User user) {
        CurrentAccount currentAccount = loadAccountPort.currentAccountInfo(user.getUsername())
                .orElseThrow(CommonApplicationException.ACCOUNT_NOT_FOUND);

        if (currentAccount.emailVerifiedAt() == null)
            throw CommonApplicationException.EXCEPTION_EMAIL_NOT_VERIFIED;
    }
}
