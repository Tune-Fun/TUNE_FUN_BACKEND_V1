package com.tune_fun.v1.account.application.service;

import com.tune_fun.v1.account.application.port.input.command.AccountCommands;
import com.tune_fun.v1.account.application.port.input.usecase.SendForgotPasswordOtpUseCase;
import com.tune_fun.v1.account.application.port.output.LoadAccountPort;
import com.tune_fun.v1.account.domain.value.CurrentAccount;
import com.tune_fun.v1.common.exception.CommonApplicationException;
import com.tune_fun.v1.common.stereotype.UseCase;
import com.tune_fun.v1.otp.application.port.output.SaveOtpPort;
import com.tune_fun.v1.otp.application.port.output.SendOtpPort;
import com.tune_fun.v1.otp.domain.behavior.SaveOtp;
import com.tune_fun.v1.otp.domain.behavior.SendOtp;
import com.tune_fun.v1.otp.domain.value.CurrentOtp;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.tune_fun.v1.otp.adapter.output.persistence.OtpType.FORGOT_PASSWORD;


@Service
@UseCase
@RequiredArgsConstructor
public class SendForgotPasswordOtpService implements SendForgotPasswordOtpUseCase {

    private final LoadAccountPort loadAccountPort;
    private final SaveOtpPort saveOtpPort;
    private final SendOtpPort sendOtpPort;

    @NotNull
    private static SaveOtp getSaveOtp(String username) {
        return new SaveOtp(username, FORGOT_PASSWORD.getLabel());
    }

    @Override
    @Transactional
    public void sendOtp(final AccountCommands.SendForgotPasswordOtp command) throws Exception {
        CurrentAccount currentAccount = getCurrentAccount(command);

        SaveOtp saveOtp = getSaveOtp(currentAccount.username());
        CurrentOtp currentOtp = saveOtpPort.saveOtp(saveOtp);

        SendOtp sendOtp = new SendOtp(currentAccount.email(), currentAccount.nickname(), currentOtp.token());
        sendOtpPort.sendOtp(sendOtp);
    }

    @Transactional(readOnly = true)
    public CurrentAccount getCurrentAccount(final AccountCommands.SendForgotPasswordOtp command) {
        return loadAccountPort.currentAccountInfo(command.username())
                .orElseThrow(CommonApplicationException.ACCOUNT_NOT_FOUND);
    }
}
