package com.habin.demo.account.application.service;

import com.habin.demo.account.application.port.input.command.AccountCommands;
import com.habin.demo.account.application.port.input.usecase.RefreshUseCase;
import com.habin.demo.account.application.port.output.jwt.ReissueAccessTokenPort;
import com.habin.demo.common.hexagon.UseCase;
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
