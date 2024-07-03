package com.tune_fun.v1.otp.application.service;

import com.tune_fun.v1.account.application.port.output.LoadAccountPort;
import com.tune_fun.v1.account.application.port.output.RecordEmailVerifiedAtPort;
import com.tune_fun.v1.account.application.port.output.jwt.CreateAccessTokenPort;
import com.tune_fun.v1.account.application.port.output.jwt.CreateRefreshTokenPort;
import com.tune_fun.v1.account.domain.behavior.SaveJwtToken;
import com.tune_fun.v1.account.domain.value.CurrentAccount;
import com.tune_fun.v1.common.constant.Constants;
import com.tune_fun.v1.common.exception.CommonApplicationException;
import com.tune_fun.v1.common.stereotype.UseCase;
import com.tune_fun.v1.otp.application.port.input.query.OtpQueries;
import com.tune_fun.v1.otp.application.port.input.usecase.VerifyOtpUseCase;
import com.tune_fun.v1.otp.application.port.output.VerifyOtpPort;
import com.tune_fun.v1.otp.domain.behavior.VerifyOtp;
import com.tune_fun.v1.otp.domain.value.VerifyResult;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@UseCase
@RequiredArgsConstructor
public class VerifyOtpService implements VerifyOtpUseCase {

    private final VerifyOtpPort verifyOtpPort;
    private final LoadAccountPort loadAccountPort;
    private final RecordEmailVerifiedAtPort recordEmailVerifiedAtPort;

    private final CreateAccessTokenPort createAccessTokenPort;
    private final CreateRefreshTokenPort createRefreshTokenPort;

    private static VerifyOtp getVerifyOtpBehavior(final OtpQueries.Verify query) {
        return new VerifyOtp(query.username(), query.otpType(), query.otp());
    }

    @NotNull
    private static SaveJwtToken getSaveJwtTokenBehavior(CurrentAccount currentAccount) {
        String authorities = String.join(Constants.COMMA, currentAccount.roles());
        return new SaveJwtToken(currentAccount.username(), authorities);
    }

    @Override
    @Transactional
    public VerifyResult verify(final OtpQueries.Verify query) throws Exception {
        CurrentAccount currentAccount = getCurrentAccount(query);

        VerifyOtp verifyOtpBehavior = getVerifyOtpBehavior(query);
        verifyOtpPort.verifyOtp(verifyOtpBehavior);

        recordEmailVerifiedAtPort.recordEmailVerifiedAt(query.username());

        SaveJwtToken saveJwtTokenBehavior = getSaveJwtTokenBehavior(currentAccount);

        String accessToken = createAccessTokenPort.createAccessToken(saveJwtTokenBehavior);
        String refreshToken = createRefreshTokenPort.createRefreshToken(saveJwtTokenBehavior);

        return new VerifyResult(accessToken, refreshToken);
    }

    @Transactional(readOnly = true)
    public CurrentAccount getCurrentAccount(OtpQueries.Verify query) {
        return loadAccountPort.currentAccountInfo(query.username())
                .orElseThrow(CommonApplicationException.ACCOUNT_NOT_FOUND);
    }
}
