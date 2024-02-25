package com.tune_fun.v1.vote.adapter.input.rest;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import com.tune_fun.v1.common.config.Uris;
import com.tune_fun.v1.common.hexagon.WebAdapter;
import com.tune_fun.v1.common.response.Response;
import com.tune_fun.v1.common.response.ResponseMapper;
import com.tune_fun.v1.vote.application.port.input.command.VoteCommands;
import com.tune_fun.v1.vote.application.port.input.usecase.RegisterVoteUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@XRayEnabled
@RestController
@WebAdapter
@RequiredArgsConstructor
public class RegisterVoteController {

    private final RegisterVoteUseCase registerVoteUseCase;
    private final ResponseMapper responseMapper;

    @PostMapping(value = Uris.REGISTER_VOTE)
    public ResponseEntity<Response<?>> registerVote(@Valid @RequestBody VoteCommands.Register command) {
        registerVoteUseCase.register(command);
        return responseMapper.ok();
    }

}
