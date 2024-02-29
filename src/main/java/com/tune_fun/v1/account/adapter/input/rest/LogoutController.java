package com.tune_fun.v1.account.adapter.input.rest;

import com.tune_fun.v1.account.application.port.input.usecase.LogoutUseCase;
import com.tune_fun.v1.account.domain.state.CurrentUser;
import com.tune_fun.v1.common.config.Uris;
import com.tune_fun.v1.common.exception.CommonApplicationException;
import com.tune_fun.v1.common.hexagon.WebAdapter;
import com.tune_fun.v1.common.response.Response;
import com.tune_fun.v1.common.response.ResponseMapper;
import com.tune_fun.v1.common.util.StringUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

import static com.tune_fun.v1.common.response.MessageCode.EXCEPTION_AUTHENTICATION_TOKEN_NOT_FOUND;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@WebAdapter
@RequiredArgsConstructor
public class LogoutController {

    private final LogoutUseCase logoutUseCase;
    private final ResponseMapper responseMapper;

    @PutMapping(value = Uris.LOGOUT)
    public ResponseEntity<Response<?>> logout(final HttpServletRequest request, @CurrentUser final User user) {
        String authorizationValue = Optional.ofNullable(request.getHeader(AUTHORIZATION))
                .orElseThrow(() -> new CommonApplicationException(EXCEPTION_AUTHENTICATION_TOKEN_NOT_FOUND));
        String accessToken = StringUtil.removeBearerPrefix(authorizationValue);

        logoutUseCase.logout(accessToken);
        return responseMapper.ok();
    }

}
