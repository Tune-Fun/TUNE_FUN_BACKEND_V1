package com.tune_fun.v1.account.adapter.input.rest;

import com.tune_fun.v1.account.application.port.input.command.AccountCommands;
import com.tune_fun.v1.account.application.port.input.usecase.OAuth2LinkUseCase;
import com.tune_fun.v1.account.domain.state.CurrentUser;
import com.tune_fun.v1.common.config.Uris;
import com.tune_fun.v1.common.response.BasePayload;
import com.tune_fun.v1.common.response.MessageCode;
import com.tune_fun.v1.common.response.Response;
import com.tune_fun.v1.common.response.ResponseMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequiredArgsConstructor
public class OAuth2LinkController {

    private final OAuth2LinkUseCase oAuth2LinkUseCase;
    private final ResponseMapper responseMapper;

    /**
     * Link Google OAuth2
     * @param command 연결될 Google OAuth2 계정의 이메일
     * @param user 연결할 사용자
     * @return responseMapper.ok(MessageCode.SUCCESS_LINK_GOOGLE_OAUTH2)
     */
    @PatchMapping(value = Uris.LINK_GOOGLE)
    public ResponseEntity<Response<BasePayload>> linkGoogle(@RequestBody @Valid final AccountCommands.OAuth2Link command, @CurrentUser final User user) {
        oAuth2LinkUseCase.linkGoogle(command, user);
        return responseMapper.ok(MessageCode.SUCCESS_LINK_GOOGLE_OAUTH2);
    }

}
