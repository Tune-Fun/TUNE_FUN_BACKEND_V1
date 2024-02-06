package com.habin.demo.account.adapter.input.rest;

import com.habin.demo.account.application.port.input.usecase.LogoutUseCase;
import com.habin.demo.account.domain.state.CurrentUser;
import com.habin.demo.common.config.Uris;
import com.habin.demo.common.exception.CommonApplicationException;
import com.habin.demo.common.hexagon.WebAdapter;
import com.habin.demo.common.response.MessageCode;
import com.habin.demo.common.response.Response;
import com.habin.demo.common.response.ResponseMapper;
import com.habin.demo.common.util.StringUtil;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@SecurityRequirement(name = AUTHORIZATION)
@RestController
@WebAdapter
@RequiredArgsConstructor
public class LogoutController {

    private final LogoutUseCase logoutUseCase;
    private final ResponseMapper responseMapper;

    @PutMapping(value = Uris.LOGOUT)
    public ResponseEntity<Response<?>> logout(
            @RequestHeader(value = AUTHORIZATION) String authorizationValue,
            @CurrentUser final User user) {
        if (authorizationValue == null)
            throw new CommonApplicationException(MessageCode.EXCEPTION_AUTHENTICATION_TOKEN_NOT_FOUND);
        String accessToken = StringUtil.removeBearerPrefix(authorizationValue);

        logoutUseCase.logout(accessToken);
        return responseMapper.ok();
    }

}
