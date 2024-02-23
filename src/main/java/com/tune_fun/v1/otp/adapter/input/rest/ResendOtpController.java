package com.tune_fun.v1.otp.adapter.input.rest;

import com.tune_fun.v1.common.config.Uris;
import com.tune_fun.v1.common.hexagon.WebAdapter;
import com.tune_fun.v1.common.response.BasePayload;
import com.tune_fun.v1.common.response.Response;
import com.tune_fun.v1.common.response.ResponseMapper;
import com.tune_fun.v1.otp.application.port.input.command.OtpCommands;
import com.tune_fun.v1.otp.application.port.input.usecase.ResendOtpUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.tune_fun.v1.common.response.MessageCode.SUCCESS_OTP_RESEND;

@RestController
@WebAdapter
@RequiredArgsConstructor
public class ResendOtpController {

    private final ResendOtpUseCase resendOtpUseCase;
    private final ResponseMapper responseMapper;

    @PutMapping(value = Uris.RESEND_OTP)
    public ResponseEntity<Response<BasePayload>> resendOtp(@Valid @RequestBody final OtpCommands.Resend command) throws Exception {
        resendOtpUseCase.resend(command);
        return responseMapper.ok(SUCCESS_OTP_RESEND);
    }

}
