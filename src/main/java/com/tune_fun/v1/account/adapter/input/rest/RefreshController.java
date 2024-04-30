package com.tune_fun.v1.account.adapter.input.rest;

import com.tune_fun.v1.account.application.port.input.command.AccountCommands;
import com.tune_fun.v1.account.application.port.input.usecase.RefreshUseCase;
import com.tune_fun.v1.account.domain.value.ReissuedAccessToken;
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
public class RefreshController {

    private final RefreshUseCase refreshUseCase;
    private final ResponseMapper responseMapper;

    @PostMapping(value = Uris.REFRESH)
    public ResponseEntity<Response<ReissuedAccessToken>> refresh(@Valid @RequestBody final AccountCommands.Refresh command) {
        String accessToken = refreshUseCase.refresh(command);
        return responseMapper.ok(new ReissuedAccessToken(accessToken));
    }

}
