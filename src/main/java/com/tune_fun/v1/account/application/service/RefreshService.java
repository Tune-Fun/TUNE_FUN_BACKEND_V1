package com.tune_fun.v1.account.application.service;

import com.tune_fun.v1.account.application.port.input.command.AccountCommands;
import com.tune_fun.v1.account.application.port.input.usecase.RefreshUseCase;
import com.tune_fun.v1.account.application.port.output.jwt.ReissueAccessTokenPort;
import com.tune_fun.v1.common.hexagon.UseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@UseCase
@RequiredArgsConstructor
public class RefreshService implements RefreshUseCase {

    private final ReissueAccessTokenPort reissueAccessTokenPort;

    @Override
    public String refresh(AccountCommands.Refresh command) {
        return reissueAccessTokenPort.reissueAccessToken(command.refreshToken());
    }

}
