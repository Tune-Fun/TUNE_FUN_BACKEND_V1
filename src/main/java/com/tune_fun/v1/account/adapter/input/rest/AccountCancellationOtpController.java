package com.tune_fun.v1.account.adapter.input.rest;

import com.tune_fun.v1.account.application.port.input.usecase.SendAccountCancellationOtpUseCase;
import com.tune_fun.v1.account.domain.value.CurrentUser;
import com.tune_fun.v1.common.config.Uris;
import com.tune_fun.v1.common.response.BasePayload;
import com.tune_fun.v1.common.response.MessageCode;
import com.tune_fun.v1.common.response.Response;
import com.tune_fun.v1.common.response.ResponseMapper;
import com.tune_fun.v1.common.stereotype.WebAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PostMapping;

@WebAdapter
@RequiredArgsConstructor
public class AccountCancellationOtpController {

    private final SendAccountCancellationOtpUseCase sendAccountCancellationOtpUseCase;
    private final ResponseMapper responseMapper;

    @PostMapping(value = Uris.ACCOUNT_CANCELLATION_SEND_OTP)
    public ResponseEntity<Response<BasePayload>> sendAccountCancellationOtp(@CurrentUser User user) throws Exception {
        sendAccountCancellationOtpUseCase.sendOtp(user.getUsername());
        return responseMapper.ok(MessageCode.SUCCESS_ACCOUNT_CANCELLATION_OTP_SENT);
    }
}

