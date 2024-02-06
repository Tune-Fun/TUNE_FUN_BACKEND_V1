package com.habin.demo.account.application.service.jwt;

import com.habin.demo.account.application.port.input.usecase.jwt.ValidateAccessTokenUseCase;
import com.habin.demo.account.application.port.output.jwt.CheckAccessTokenExpirePort;
import com.habin.demo.account.application.port.output.jwt.ValidateJwtTokenPort;
import com.habin.demo.common.hexagon.UseCase;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class ValidateAccessTokenService implements ValidateAccessTokenUseCase {

    private final ValidateJwtTokenPort validateJwtTokenPort;
    private final CheckAccessTokenExpirePort checkAccessTokenExpirePort;

    @Override
    public Boolean validateAccessToken(String accessToken) {
        return accessToken != null
                && validateJwtTokenPort.validate(accessToken)
                && !checkAccessTokenExpirePort.isAccessTokenExpired(accessToken);
    }
}
