package com.habin.demo.account.application.service.jwt;

import com.habin.demo.account.application.port.input.usecase.jwt.LoadUsernameFromTokenUseCase;
import com.habin.demo.account.application.port.output.jwt.LoadUsernamePort;
import com.habin.demo.common.hexagon.UseCase;
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
