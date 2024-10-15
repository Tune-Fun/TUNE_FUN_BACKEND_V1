package com.tune_fun.v1.account.application.service;

import com.tune_fun.v1.account.application.port.input.usecase.SendAccountCancellationOtpUseCase;
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

import static com.tune_fun.v1.otp.domain.behavior.OtpType.ACCOUNT_CANCELLATION;


@Service
@UseCase
@RequiredArgsConstructor
public class SendAccountCancellationOtpService implements SendAccountCancellationOtpUseCase {

    private final LoadAccountPort loadAccountPort;
    private final SaveOtpPort saveOtpPort;
    private final SendOtpPort sendOtpPort;

    @NotNull
    private static SaveOtp getSaveOtp(String username) {
        return new SaveOtp(username, ACCOUNT_CANCELLATION.getLabel());
    }

    @Override
    @Transactional
    public void sendOtp(final String username) throws Exception {
        CurrentAccount currentAccount = getCurrentAccount(username);

        SaveOtp saveOtp = getSaveOtp(currentAccount.username());
        CurrentOtp currentOtp = saveOtpPort.saveOtp(saveOtp);

        SendOtp sendOtp = new SendOtp(currentAccount.email(), currentAccount.nickname(), currentOtp.token());
        sendOtpPort.sendOtp(sendOtp);
    }

    private CurrentAccount getCurrentAccount(String username) {
        return loadAccountPort.currentAccountInfo(username)
                .orElseThrow(CommonApplicationException.ACCOUNT_NOT_FOUND);
    }
}
