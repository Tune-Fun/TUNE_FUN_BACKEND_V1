package com.tune_fun.v1.vote.adapter.input.rest;

import com.tune_fun.v1.account.domain.value.CurrentUser;
import com.tune_fun.v1.common.config.Uris;
import com.tune_fun.v1.common.response.Response;
import com.tune_fun.v1.common.response.ResponseMapper;
import com.tune_fun.v1.common.stereotype.WebAdapter;
import com.tune_fun.v1.vote.application.port.input.usecase.RegisterVoteUseCase;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


@WebAdapter
@RequiredArgsConstructor
public class VoteController {

    private final RegisterVoteUseCase registerVoteUseCase;
    private final ResponseMapper responseMapper;

    //    @PreAuthorize("hasPermission(#votePaperId, 'VOTE', 'REGISTER_VOTE')")
    @PostMapping(value = Uris.REGISTER_VOTE)
    public ResponseEntity<Response<?>> registerVote(@PathVariable(name = "votePaperId") @NotNull(message = "{vote.paper.id.not_null}") final Long votePaperId,
                                                    @PathVariable(name = "voteChoiceId") @NotNull(message = "{vote.choice.id.not_null}") final Long voteChoiceId,
                                                    @CurrentUser final User user) {
        registerVoteUseCase.register(votePaperId, voteChoiceId, user);
        return responseMapper.ok();
    }

}
