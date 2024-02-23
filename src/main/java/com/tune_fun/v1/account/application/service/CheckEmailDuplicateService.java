package com.tune_fun.v1.account.application.service;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import com.tune_fun.v1.account.application.port.input.usecase.CheckEmailDuplicateUseCase;
import com.tune_fun.v1.account.application.port.output.LoadAccountPort;
import com.tune_fun.v1.common.exception.CommonApplicationException;
import com.tune_fun.v1.common.hexagon.UseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.tune_fun.v1.common.response.MessageCode.USER_POLICY_EMAIL_REGISTERED;

@XRayEnabled
@Service
@UseCase
@RequiredArgsConstructor
public class CheckEmailDuplicateService implements CheckEmailDuplicateUseCase {

    private final LoadAccountPort loadAccountPort;

    @Override
    @Transactional(readOnly = true)
    public void checkEmailDuplicate(final String email) {
        loadAccountPort.registeredAccountInfoByEmail(email)
                .ifPresent(account -> {throw new CommonApplicationException(USER_POLICY_EMAIL_REGISTERED);});
    }
}
