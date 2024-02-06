package com.habin.demo.account.adapter.input.rest;

import com.habin.demo.account.application.port.input.command.AccountCommands;
import com.habin.demo.account.application.port.input.usecase.LoginUseCase;
import com.habin.demo.account.domain.state.LoginResult;
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
public class LoginController {

    private final LoginUseCase loginUseCase;
    private final ResponseMapper responseMapper;

    @PostMapping(value = Uris.LOGIN)
    public ResponseEntity<Response<LoginResult>> login(@Valid @RequestBody final AccountCommands.Login command) {
        LoginResult loginResult = loginUseCase.login(command);
        return responseMapper.ok(loginResult);
    }

}
