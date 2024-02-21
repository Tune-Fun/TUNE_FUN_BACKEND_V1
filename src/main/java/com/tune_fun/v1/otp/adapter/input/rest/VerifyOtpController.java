package com.tune_fun.v1.otp.adapter.input.rest;

import com.tune_fun.v1.common.hexagon.WebAdapter;
import com.tune_fun.v1.common.response.ResponseMapper;
import com.tune_fun.v1.otp.application.port.input.usecase.VerifyOtpUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@WebAdapter
@RequiredArgsConstructor
public class VerifyOtpController {

    private final VerifyOtpUseCase verifyOtpUseCase;
    private final ResponseMapper responseMapper;


}
