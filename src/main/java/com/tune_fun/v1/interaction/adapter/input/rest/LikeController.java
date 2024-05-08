package com.tune_fun.v1.interaction.adapter.input.rest;

import com.tune_fun.v1.account.domain.value.CurrentUser;
import com.tune_fun.v1.common.config.Uris;
import com.tune_fun.v1.common.hexagon.WebAdapter;
import com.tune_fun.v1.interaction.application.port.input.usecase.LikeVotePaperUseCase;
import com.tune_fun.v1.interaction.application.port.input.usecase.UnlikeVotePaperUseCase;
import lombok.RequiredArgsConstructor;
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

    @PostMapping(value = Uris.LIKE_VOTE_PAPER)
    public void likeVotePaper(@PathVariable(name = "votePaperId") String votePaperId, @CurrentUser final User user) {

    }

    @DeleteMapping(value = Uris.LIKE_VOTE_PAPER)
    public void unlikeVotePaper(@PathVariable(name = "votePaperId") String votePaperId, @CurrentUser final User user) {

    }

}
