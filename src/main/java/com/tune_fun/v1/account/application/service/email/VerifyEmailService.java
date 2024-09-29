package com.tune_fun.v1.account.application.service.email;

import com.tune_fun.v1.account.application.port.input.usecase.email.VerifyEmailUseCase;
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
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.tune_fun.v1.otp.domain.behavior.OtpType.VERIFY_EMAIL;

@Service
@UseCase
@RequiredArgsConstructor
public class VerifyEmailService implements VerifyEmailUseCase {

    private final LoadAccountPort loadAccountPort;

    private final SaveOtpPort saveOtpPort;
    private final SendOtpPort sendOtpPort;

    @Transactional
    @Override
    public void verifyEmail(final User user) throws Exception {
        CurrentAccount currentAccount = getCurrentAccount(user.getUsername());

        SaveOtp saveOtp = getSaveOtp(user.getUsername());
        CurrentOtp currentOtp = saveOtpPort.saveOtp(saveOtp);

        SendOtp sendOtp = new SendOtp(currentAccount.email(), currentAccount.nickname(), currentOtp.token());
        sendOtpPort.sendOtp(sendOtp);
    }

    @NotNull
    private static SaveOtp getSaveOtp(String username) {
        return new SaveOtp(username, VERIFY_EMAIL.getLabel());
    }

    @Transactional(readOnly = true)
    public CurrentAccount getCurrentAccount(final String username) {
        return loadAccountPort.currentAccountInfo(username)
                .orElseThrow(CommonApplicationException.ACCOUNT_NOT_FOUND);
    }
}
