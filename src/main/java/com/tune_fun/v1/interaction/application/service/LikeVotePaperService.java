package com.tune_fun.v1.interaction.application.service;

import com.tune_fun.v1.common.hexagon.UseCase;
import com.tune_fun.v1.interaction.application.port.input.usecase.LikeVotePaperUseCase;
import com.tune_fun.v1.interaction.application.port.output.SaveLikePort;
import com.tune_fun.v1.interaction.application.port.output.SaveVotePaperLikeCountPort;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@UseCase
@RequiredArgsConstructor
public class LikeVotePaperService implements LikeVotePaperUseCase {

    private final SaveLikePort saveLikePort;
    private final SaveVotePaperLikeCountPort saveVotePaperLikeCountPort;


    @Transactional
    @Override
    public void likeVotePaper(final Long votePaperId, final User user) {
        saveLikePort.saveVotePaperLike(votePaperId, user.getUsername());
        saveVotePaperLikeCountPort.incrementVotePaperLikeCount(votePaperId);
    }

}
