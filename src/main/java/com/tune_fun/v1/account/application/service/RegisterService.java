package com.tune_fun.v1.account.application.service;

import com.tune_fun.v1.account.application.port.input.command.AccountCommands;
import com.tune_fun.v1.account.application.port.input.usecase.RegisterUseCase;
import com.tune_fun.v1.account.application.port.output.LoadAccountPort;
import com.tune_fun.v1.account.application.port.output.SaveAccountPort;
import com.tune_fun.v1.account.application.port.output.jwt.CreateAccessTokenPort;
import com.tune_fun.v1.account.application.port.output.jwt.CreateRefreshTokenPort;
import com.tune_fun.v1.account.domain.behavior.SaveAccount;
import com.tune_fun.v1.account.domain.behavior.SaveJwtToken;
import com.tune_fun.v1.account.domain.state.CurrentAccount;
import com.tune_fun.v1.account.domain.state.RegisterResult;
import com.tune_fun.v1.common.exception.CommonApplicationException;
import com.tune_fun.v1.common.hexagon.UseCase;
import com.tune_fun.v1.common.util.StringUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.tune_fun.v1.common.response.MessageCode.USER_POLICY_ACCOUNT_REGISTERED;


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
