package com.tune_fun.v1.account.application.service;

import com.tune_fun.v1.account.application.port.input.usecase.CheckUsernameDuplicateUseCase;
import com.tune_fun.v1.account.application.port.output.LoadAccountPort;
import com.tune_fun.v1.common.exception.CommonApplicationException;
import com.tune_fun.v1.common.stereotype.UseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@UseCase
@RequiredArgsConstructor
public class CheckUsernameDuplicateService implements CheckUsernameDuplicateUseCase {

    private final LoadAccountPort loadAccountPort;

    @Override
    @Transactional(readOnly = true)
    public void checkUsernameDuplicate(final String username) {
        loadAccountPort.registeredAccountInfoByUsername(username)
                .ifPresent(account -> {
                    throw CommonApplicationException.USER_POLICY_USERNAME_REGISTERED;
                });
    }
}
