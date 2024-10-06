package com.tune_fun.v1.account.adapter.input.rest;


import com.tune_fun.v1.account.application.port.input.query.AccountQueries;
import com.tune_fun.v1.account.application.port.input.usecase.CheckPasswordMatchUseCase;
import com.tune_fun.v1.account.domain.value.CheckPasswordMathResult;
import com.tune_fun.v1.account.domain.value.CurrentUser;
import com.tune_fun.v1.common.config.Uris;
import com.tune_fun.v1.common.response.BasePayload;
import com.tune_fun.v1.common.response.Response;
import com.tune_fun.v1.common.response.ResponseMapper;
import com.tune_fun.v1.common.stereotype.WebAdapter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@WebAdapter
@RequiredArgsConstructor
public class CheckPasswordMatchController {

    private final CheckPasswordMatchUseCase checkPasswordMatchUseCase;
    private final ResponseMapper responseMapper;

    @PostMapping(Uris.CHECK_PASSWORD_MATCH)
    public ResponseEntity<Response<BasePayload>> checkPasswordMatch(
            @RequestBody @Valid final AccountQueries.Password password, @CurrentUser User user) {
        CheckPasswordMathResult checkPasswordMathResult = checkPasswordMatchUseCase.checkPasswordMatch(password, user.getUsername());
        return responseMapper.ok(checkPasswordMathResult);
    }
}
