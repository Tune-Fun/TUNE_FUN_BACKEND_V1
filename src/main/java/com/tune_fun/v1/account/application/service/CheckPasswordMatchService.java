package com.tune_fun.v1.account.application.service;

import com.tune_fun.v1.account.application.port.input.query.AccountQueries;
import com.tune_fun.v1.account.application.port.input.usecase.CheckPasswordMatchUseCase;
import com.tune_fun.v1.account.application.port.output.LoadAccountPort;
import com.tune_fun.v1.account.domain.value.CheckPasswordMathResult;
import com.tune_fun.v1.account.domain.value.RegisteredAccount;
import com.tune_fun.v1.common.exception.CommonApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CheckPasswordMatchService implements CheckPasswordMatchUseCase {

    private final LoadAccountPort loadAccountPort;
    private final PasswordEncoder passwordEncoder;

    @Override
    public CheckPasswordMathResult checkPasswordMatch(AccountQueries.Password query, String username) {
        RegisteredAccount registeredAccount = loadAccountPort.registeredAccountInfoByUsername(username)
                .orElseThrow(CommonApplicationException.ACCOUNT_NOT_FOUND);

        return new CheckPasswordMathResult(passwordEncoder.matches(query.password(), registeredAccount.password()));
    }
}
