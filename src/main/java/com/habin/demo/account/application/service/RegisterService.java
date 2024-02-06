package com.habin.demo.account.application.service;

import com.habin.demo.account.application.port.input.command.AccountCommands;
import com.habin.demo.account.application.port.input.usecase.RegisterUseCase;
import com.habin.demo.account.application.port.output.LoadAccountPort;
import com.habin.demo.account.application.port.output.SaveAccountPort;
import com.habin.demo.account.application.port.output.jwt.CreateAccessTokenPort;
import com.habin.demo.account.application.port.output.jwt.CreateRefreshTokenPort;
import com.habin.demo.account.domain.behavior.SaveAccount;
import com.habin.demo.account.domain.behavior.SaveJwtToken;
import com.habin.demo.account.domain.state.CurrentAccount;
import com.habin.demo.account.domain.state.RegisterResult;
import com.habin.demo.common.exception.CommonApplicationException;
import com.habin.demo.common.hexagon.UseCase;
import com.habin.demo.common.util.StringUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.habin.demo.common.response.MessageCode.USER_POLICY_ACCOUNT_REGISTERED;

@Service
@UseCase
@RequiredArgsConstructor
public class RegisterService implements RegisterUseCase {

    private final LoadAccountPort loadAccountPort;
    private final SaveAccountPort saveAccountPort;
    private final CreateAccessTokenPort createAccessTokenPort;
    private final CreateRefreshTokenPort createRefreshTokenPort;

    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public RegisterResult register(final AccountCommands.Register command) {
        loadAccountPort.accountInfo(command.username()).ifPresent(accountInfo -> {
            throw new CommonApplicationException(USER_POLICY_ACCOUNT_REGISTERED);
        });

        String encodedPassword = passwordEncoder.encode(command.password());
        SaveAccount saveAccount = new SaveAccount(
                StringUtil.uuid(), command.username(), encodedPassword,
                command.email(), command.nickname());
        CurrentAccount savedAccount = saveAccountPort.saveAccount(saveAccount);

        String authorities = String.join(",", savedAccount.roles());

        SaveJwtToken saveJwtToken = new SaveJwtToken(savedAccount.username(), authorities);

        String accessToken = createAccessTokenPort.createAccessToken(saveJwtToken);
        String refreshToken = createRefreshTokenPort.createRefreshToken(saveJwtToken);

        return new RegisterResult(savedAccount.username(), savedAccount.roles(), accessToken, refreshToken);
    }
}
