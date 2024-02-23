package com.tune_fun.v1.otp.application.service;

import com.tune_fun.v1.account.application.port.output.LoadAccountPort;
import com.tune_fun.v1.account.domain.state.CurrentAccount;
import com.tune_fun.v1.common.exception.CommonApplicationException;
import com.tune_fun.v1.common.hexagon.UseCase;
import com.tune_fun.v1.common.response.MessageCode;
import com.tune_fun.v1.otp.application.port.input.command.OtpCommands;
import com.tune_fun.v1.otp.application.port.input.usecase.ResendOtpUseCase;
import com.tune_fun.v1.otp.application.port.output.DeleteOtpPort;
import com.tune_fun.v1.otp.application.port.output.SaveOtpPort;
import com.tune_fun.v1.otp.application.port.output.SendOtpPort;
import com.tune_fun.v1.otp.domain.behavior.SaveOtp;
import com.tune_fun.v1.otp.domain.behavior.SendOtp;
import com.tune_fun.v1.otp.domain.state.CurrentOtp;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@UseCase
@RequiredArgsConstructor
public class ResendOtpService implements ResendOtpUseCase {

    private final LoadAccountPort loadAccountPort;
    private final DeleteOtpPort deleteOtpPort;
    private final SaveOtpPort saveOtpPort;
    private final SendOtpPort sendOtpPort;

    @Override
    public void resend(final OtpCommands.Resend command) throws Exception {
        Optional<CurrentAccount> currentAccount = loadAccountPort.currentAccountInfo(command.username());
        if (currentAccount.isEmpty()) throw new CommonApplicationException(MessageCode.ACCOUNT_NOT_FOUND);

        CurrentAccount account = currentAccount.get();

        deleteOtpPort.expire(command.otpType(), command.username());

        SaveOtp saveOtpBehavior = new SaveOtp(command.username(), command.otpType());
        CurrentOtp currentOtp = saveOtpPort.saveOtp(saveOtpBehavior);

        SendOtp sendOtpBehavior = new SendOtp(account.email(), account.nickname(), currentOtp.token());
        sendOtpPort.sendOtp(sendOtpBehavior);
    }
}
