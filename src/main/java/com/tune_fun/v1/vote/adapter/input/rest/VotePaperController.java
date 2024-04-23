package com.tune_fun.v1.vote.adapter.input.rest;

import com.tune_fun.v1.common.config.Uris;
import com.tune_fun.v1.common.response.Response;
import com.tune_fun.v1.common.response.ResponseMapper;
import com.tune_fun.v1.vote.application.port.input.command.VoteCommands;
import com.tune_fun.v1.vote.application.port.input.command.VotePaperCommands;
import com.tune_fun.v1.vote.application.port.input.usecase.RegisterVotePaperUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class VotePaperController {

    private final ResponseMapper responseMapper;
    private final RegisterVotePaperUseCase registerVotePaperUseCase;

    @PostMapping(value = Uris.REGISTER_VOTE_PAPER)
    public ResponseEntity<Response<?>> registerVoteArticle(@Valid @RequestBody final VotePaperCommands.Register command) {
        registerVotePaperUseCase.register(command);
        return responseMapper.ok();
    }

}
