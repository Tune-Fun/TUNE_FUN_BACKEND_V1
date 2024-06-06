package com.tune_fun.v1.account.application.service.jwt;

import com.tune_fun.v1.account.application.port.input.usecase.jwt.ValidateAccessTokenUseCase;
import com.tune_fun.v1.account.application.port.output.jwt.CheckAccessTokenExpirePort;
import com.tune_fun.v1.account.application.port.output.jwt.ValidateJwtTokenPort;
import com.tune_fun.v1.common.stereotype.UseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
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
