package com.tune_fun.v1.account.application.service;

import com.tune_fun.v1.account.application.port.input.command.AccountCommands;
import com.tune_fun.v1.account.application.port.input.usecase.RegisterUseCase;
import com.tune_fun.v1.account.application.port.output.LoadAccountPort;
import com.tune_fun.v1.account.application.port.output.SaveAccountPort;
import com.tune_fun.v1.account.application.port.output.jwt.CreateAccessTokenPort;
import com.tune_fun.v1.account.application.port.output.jwt.CreateRefreshTokenPort;
import com.tune_fun.v1.account.domain.behavior.SaveAccount;
import com.tune_fun.v1.account.domain.behavior.SaveJwtToken;
import com.tune_fun.v1.account.domain.value.CurrentAccount;
import com.tune_fun.v1.account.domain.value.RegisterResult;
import com.tune_fun.v1.common.constant.Constants;
import com.tune_fun.v1.common.exception.CommonApplicationException;
import com.tune_fun.v1.common.stereotype.UseCase;
import com.tune_fun.v1.common.util.StringUtil;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@UseCase
@RequiredArgsConstructor
public class RegisterService implements RegisterUseCase {

    private final LoadAccountPort loadAccountPort;
    private final SaveAccountPort saveAccountPort;
    private final CreateAccessTokenPort createAccessTokenPort;
    private final CreateRefreshTokenPort createRefreshTokenPort;

    private final PasswordEncoder passwordEncoder;

    @NotNull
    private static SaveAccount getSaveAccount(final String registerType, final AccountCommands.Register command, final String encodedPassword) {
        return new SaveAccount(registerType,
                StringUtil.uuid(), command.username(), encodedPassword,
                command.email(), command.nickname(), command.notification().voteDeliveryNotification(),
                command.notification().voteEndNotification(), command.notification().voteDeliveryNotification()
        );
    }

    @NotNull
    private static RegisterResult getRegisterResult(CurrentAccount savedAccount, String accessToken, String refreshToken) {
        return new RegisterResult(savedAccount.username(), savedAccount.roles(), accessToken, refreshToken);
    }

    @Override
    @Transactional
    public RegisterResult register(final String registerType, final AccountCommands.Register command) {
        checkRegisteredAccount(command);

        String encodedPassword = passwordEncoder.encode(command.password());
        SaveAccount saveAccount = getSaveAccount(registerType, command, encodedPassword);
        CurrentAccount savedAccount = saveAccountPort.saveAccount(saveAccount);

        String authorities = String.join(Constants.COMMA, savedAccount.roles());

        SaveJwtToken saveJwtToken = new SaveJwtToken(savedAccount.username(), authorities);

        String accessToken = createAccessTokenPort.createAccessToken(saveJwtToken);
        String refreshToken = createRefreshTokenPort.createRefreshToken(saveJwtToken);

        return getRegisterResult(savedAccount, accessToken, refreshToken);
    }

    @Transactional(readOnly = true)
    public void checkRegisteredAccount(AccountCommands.Register command) {
        loadAccountPort.currentAccountInfo(command.username()).ifPresent(accountInfo -> {
            throw CommonApplicationException.USER_POLICY_ACCOUNT_REGISTERED;
        });
    }
}
