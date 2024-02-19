package com.tune_fun.v1.account.adapter.input.rest;

import com.tune_fun.v1.account.application.port.input.command.AccountCommands;
import com.tune_fun.v1.account.application.port.input.usecase.SendForgotPasswordOtpUseCase;
import com.tune_fun.v1.common.config.Uris;
import com.tune_fun.v1.common.hexagon.WebAdapter;
import com.tune_fun.v1.common.response.Response;
import com.tune_fun.v1.common.response.ResponseMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@WebAdapter
@RequiredArgsConstructor
public class ForgotPasswordOtpController {

    private final SendForgotPasswordOtpUseCase sendForgotPasswordOtpUseCase;
    private final ResponseMapper responseMapper;

    @PostMapping(value = Uris.FORGOT_PASSWORD_SEND_OTP)
    public ResponseEntity<Response<?>> sendForgotPasswordOtp(@Valid @RequestBody final AccountCommands.SendForgotPasswordOtp command) throws Exception {
        sendForgotPasswordOtpUseCase.sendOtp(command);
        return responseMapper.ok();
    }

}
