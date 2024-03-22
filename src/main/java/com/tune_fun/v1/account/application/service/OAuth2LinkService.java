package com.tune_fun.v1.account.application.service;

import com.tune_fun.v1.account.application.port.input.command.AccountCommands;
import com.tune_fun.v1.account.application.port.input.usecase.OAuth2LinkUseCase;
import com.tune_fun.v1.account.application.port.output.LoadAccountPort;
import com.tune_fun.v1.account.application.port.output.oauth2.LoadOAuth2AccountPort;
import com.tune_fun.v1.common.exception.AppException;
import com.tune_fun.v1.common.hexagon.UseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.tune_fun.v1.common.response.MessageCode.USER_POLICY_ALREADY_LINKED_GOOGLE_OAUTH2;

@Service
@UseCase
@RequiredArgsConstructor
public class OAuth2LinkService implements OAuth2LinkUseCase {

    private final LoadAccountPort loadAccountPort;
    private final LoadOAuth2AccountPort loadOAuth2AccountPort;

    @Override
    @Transactional
    public void linkGoogle(final AccountCommands.OAuth2Link command, final User user) {
        if (loadOAuth2AccountPort.findByOAuth2ProviderAndEmail("google", command.email()).isPresent())
            throw new AppException(USER_POLICY_ALREADY_LINKED_GOOGLE_OAUTH2);


    }

}
