package com.tune_fun.v1.otp.adapter.input.rest;

import com.tune_fun.v1.common.config.Uris;
import com.tune_fun.v1.common.response.MessageCode;
import com.tune_fun.v1.common.response.Response;
import com.tune_fun.v1.common.response.ResponseMapper;
import com.tune_fun.v1.common.stereotype.WebAdapter;
import com.tune_fun.v1.otp.application.port.input.query.OtpQueries;
import com.tune_fun.v1.otp.application.port.input.usecase.VerifyOtpUseCase;
import com.tune_fun.v1.otp.domain.value.VerifyResult;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@WebAdapter
@RequiredArgsConstructor
public class VerifyOtpController {

    private final VerifyOtpUseCase verifyOtpUseCase;
    private final ResponseMapper responseMapper;

    @PostMapping(value = Uris.VERIFY_OTP)
    public ResponseEntity<Response<VerifyResult>> verifyOtp(@Valid @RequestBody final OtpQueries.Verify query) throws Exception {
        VerifyResult verifyResult = verifyOtpUseCase.verify(query);
        return responseMapper.ok(MessageCode.SUCCESS_OTP_VERIFIED, verifyResult);
    }


}
