package com.tune_fun.v1.account.application.service.email;

import com.tune_fun.v1.account.application.port.input.usecase.email.UnlinkEmailUseCase;
import com.tune_fun.v1.account.application.port.output.RecordEmailVerifiedAtPort;
import com.tune_fun.v1.account.application.port.output.SaveEmailPort;
import com.tune_fun.v1.common.hexagon.UseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@UseCase
@RequiredArgsConstructor
public class UnlinkEmailService implements UnlinkEmailUseCase {

    private final SaveEmailPort saveEmailPort;
    private final RecordEmailVerifiedAtPort recordEmailVerifiedAtPort;

    @Transactional
    @Override
    public void unlinkEmail(final User user) {
        saveEmailPort.clearEmail(user.getUsername());
        recordEmailVerifiedAtPort.clearEmailVerifiedAt(user.getUsername());
    }

}
