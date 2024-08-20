package com.tune_fun.v1.account.application.service.email;

import com.tune_fun.v1.account.application.port.input.usecase.email.CheckEmailDuplicateUseCase;
import com.tune_fun.v1.account.application.port.output.LoadAccountPort;
import com.tune_fun.v1.common.exception.CommonApplicationException;
import com.tune_fun.v1.common.stereotype.UseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@UseCase
@RequiredArgsConstructor
public class CheckEmailDuplicateService implements CheckEmailDuplicateUseCase {

    private final LoadAccountPort loadAccountPort;

    @Override
    @Transactional(readOnly = true)
    public void checkEmailDuplicate(final String email) {
        loadAccountPort.registeredAccountInfoByEmail(email)
                .ifPresent(account -> CommonApplicationException.USER_POLICY_EMAIL_REGISTERED.run());
    }
}
