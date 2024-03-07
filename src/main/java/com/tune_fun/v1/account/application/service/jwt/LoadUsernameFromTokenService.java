package com.tune_fun.v1.account.application.service.jwt;

import com.tune_fun.v1.account.application.port.input.usecase.jwt.LoadUsernameFromTokenUseCase;
import com.tune_fun.v1.account.application.port.output.jwt.LoadUsernamePort;
import com.tune_fun.v1.common.hexagon.UseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@UseCase
@RequiredArgsConstructor
public class LoadUsernameFromTokenService implements LoadUsernameFromTokenUseCase {

    private final LoadUsernamePort loadUsernamePort;

    @Override
    public String loadUsernameFromToken(final String token) {
        return loadUsernamePort.loadUsernameByToken(token);
    }
}
