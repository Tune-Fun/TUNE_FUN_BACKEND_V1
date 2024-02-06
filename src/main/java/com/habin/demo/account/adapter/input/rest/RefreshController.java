package com.habin.demo.account.adapter.input.rest;

import com.habin.demo.account.application.port.input.command.AccountCommands;
import com.habin.demo.account.application.port.input.usecase.RefreshUseCase;
import com.habin.demo.account.domain.state.ReissuedAccessToken;
import com.habin.demo.common.config.Uris;
import com.habin.demo.common.hexagon.WebAdapter;
import com.habin.demo.common.response.Response;
import com.habin.demo.common.response.ResponseMapper;
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
