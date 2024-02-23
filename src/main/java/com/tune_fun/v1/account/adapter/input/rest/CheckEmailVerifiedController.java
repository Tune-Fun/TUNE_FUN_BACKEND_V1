package com.tune_fun.v1.account.adapter.input.rest;


import com.tune_fun.v1.account.application.port.input.usecase.CheckEmailVerifiedUseCase;
import com.tune_fun.v1.account.domain.state.CurrentUser;
import com.tune_fun.v1.common.config.Uris;
import com.tune_fun.v1.common.hexagon.WebAdapter;
import com.tune_fun.v1.common.response.BasePayload;
import com.tune_fun.v1.common.response.Response;
import com.tune_fun.v1.common.response.ResponseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.tune_fun.v1.common.response.MessageCode.SUCCESS_EMAIL_VERIFIED;

@RestController
@WebAdapter
@RequiredArgsConstructor
public class CheckEmailVerifiedController {

    private final CheckEmailVerifiedUseCase checkEmailVerifiedUseCase;
    private final ResponseMapper responseMapper;

    @GetMapping(value = Uris.CHECK_EMAIL_VERIFIED)
    public ResponseEntity<Response<BasePayload>> checkEmailVerified(@CurrentUser User user) {
        checkEmailVerifiedUseCase.checkEmailVerified(user);
        return responseMapper.ok(SUCCESS_EMAIL_VERIFIED);
    }

}
