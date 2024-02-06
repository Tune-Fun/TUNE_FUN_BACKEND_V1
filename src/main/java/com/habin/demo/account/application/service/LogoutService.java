package com.habin.demo.account.application.service;

import com.habin.demo.account.application.port.input.usecase.LogoutUseCase;
import com.habin.demo.account.application.port.output.jwt.DeleteAccessTokenPort;
import com.habin.demo.account.application.port.output.jwt.DeleteRefreshTokenPort;
import com.habin.demo.common.hexagon.UseCase;
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
