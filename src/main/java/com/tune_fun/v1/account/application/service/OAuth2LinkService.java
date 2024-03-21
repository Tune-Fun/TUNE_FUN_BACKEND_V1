package com.tune_fun.v1.account.application.service;

import com.tune_fun.v1.account.application.port.input.command.AccountCommands;
import com.tune_fun.v1.account.application.port.input.usecase.OAuth2LinkUseCase;
import com.tune_fun.v1.common.hexagon.UseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

@Service
@UseCase
@RequiredArgsConstructor
public class OAuth2LinkService implements OAuth2LinkUseCase {



    @Override
    public void linkGoogle(final AccountCommands.OAuth2Link command, final User user) {

    }


}
