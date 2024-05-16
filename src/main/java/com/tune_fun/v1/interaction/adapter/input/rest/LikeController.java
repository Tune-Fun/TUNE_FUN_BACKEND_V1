package com.tune_fun.v1.interaction.adapter.input.rest;

import com.tune_fun.v1.account.domain.value.CurrentUser;
import com.tune_fun.v1.common.config.Uris;
import com.tune_fun.v1.common.hexagon.WebAdapter;
import com.tune_fun.v1.common.response.Response;
import com.tune_fun.v1.common.response.ResponseMapper;
import com.tune_fun.v1.interaction.application.port.input.usecase.LikeVotePaperUseCase;
import com.tune_fun.v1.interaction.application.port.input.usecase.UnlikeVotePaperUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@WebAdapter
@RequiredArgsConstructor
public class LikeController {

    private final LikeVotePaperUseCase likeVotePaperUseCase;
    private final UnlikeVotePaperUseCase unlikeVotePaperUseCase;

    private final ResponseMapper responseMapper;

    @PostMapping(value = Uris.LIKE_VOTE_PAPER)
    public ResponseEntity<Response<?>> likeVotePaper(@PathVariable(name = "votePaperId") Long votePaperId, @CurrentUser final User user) {
        likeVotePaperUseCase.likeVotePaper(votePaperId, user);
        return responseMapper.ok();
    }

    @DeleteMapping(value = Uris.LIKE_ROOT + "/{votePaperId}/{likeId}")
    public ResponseEntity<Response<?>> unlikeVotePaper(
            @PathVariable(name = "votePaperId") Long votePaperId,
            @PathVariable(name = "likeId") Long likeId,
            @CurrentUser final User user) {
        unlikeVotePaperUseCase.unlikeVotePaper(votePaperId, likeId);
        return responseMapper.ok();
    }

}
