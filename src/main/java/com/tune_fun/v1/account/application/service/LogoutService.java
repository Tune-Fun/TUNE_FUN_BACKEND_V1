package com.tune_fun.v1.account.application.service;

import com.tune_fun.v1.account.application.port.input.usecase.LogoutUseCase;
import com.tune_fun.v1.account.application.port.output.jwt.DeleteAccessTokenPort;
import com.tune_fun.v1.account.application.port.output.jwt.DeleteRefreshTokenPort;
import com.tune_fun.v1.common.hexagon.UseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@UseCase
@RequiredArgsConstructor
public class LogoutService implements LogoutUseCase {

    private final DeleteAccessTokenPort deleteAccessTokenPort;
    private final DeleteRefreshTokenPort deleteRefreshTokenPort;

    @Override
    public void logout(final String accessToken) {
        deleteAccessTokenPort.deleteAccessToken(accessToken);
        deleteRefreshTokenPort.deleteRefreshToken(accessToken);
    }
}
