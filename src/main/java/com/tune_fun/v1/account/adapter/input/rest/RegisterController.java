package com.tune_fun.v1.account.adapter.input.rest;

import com.tune_fun.v1.account.application.port.input.command.AccountCommands;
import com.tune_fun.v1.account.application.port.input.usecase.RegisterUseCase;
import com.tune_fun.v1.account.domain.value.RegisterResult;
import com.tune_fun.v1.common.config.Uris;
import com.tune_fun.v1.common.hexagon.WebAdapter;
import com.tune_fun.v1.common.response.Response;
import com.tune_fun.v1.common.response.ResponseMapper;
import com.tune_fun.v1.vote.adapter.input.rest.RegisterType;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@WebAdapter
@RequiredArgsConstructor
public class RegisterController {

    private final RegisterUseCase registerUseCase;
    private final ResponseMapper responseMapper;

    /**
     * @return
     * @param<p> {@link AccountCommands.Register} command
     */
    @PostMapping(value = Uris.REGISTER)
    public ResponseEntity<Response<RegisterResult>> register(@RequestParam(name = "type") RegisterType type, @Valid @RequestBody final AccountCommands.Register command) {
        RegisterResult registerResult = registerUseCase.register(type, command);
        return responseMapper.ok(registerResult);
    }

}
