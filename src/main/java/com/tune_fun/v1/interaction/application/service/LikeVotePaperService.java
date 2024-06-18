package com.tune_fun.v1.interaction.application.service;

import com.tune_fun.v1.common.exception.CommonApplicationException;
import com.tune_fun.v1.common.stereotype.UseCase;
import com.tune_fun.v1.interaction.application.port.input.usecase.LikeVotePaperUseCase;
import com.tune_fun.v1.interaction.application.port.output.LoadLikePort;
import com.tune_fun.v1.interaction.application.port.output.SaveLikePort;
import com.tune_fun.v1.interaction.application.port.output.SaveVotePaperLikeCountPort;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.tune_fun.v1.common.response.MessageCode.VOTE_POLICY_ALREADY_LIKED_VOTE_PAPER;

@Service
@UseCase
@RequiredArgsConstructor
public class LikeVotePaperService implements LikeVotePaperUseCase {

    private final SaveLikePort saveLikePort;
    private final LoadLikePort loadLikePort;
    private final SaveVotePaperLikeCountPort saveVotePaperLikeCountPort;


    @Transactional
    @Override
    public void likeVotePaper(final Long votePaperId, final User user) {
        if (isVotePaperLikePresent(votePaperId, user))
            throw new CommonApplicationException(VOTE_POLICY_ALREADY_LIKED_VOTE_PAPER);

        saveLikePort.saveVotePaperLike(votePaperId, user.getUsername());
        saveVotePaperLikeCountPort.incrementVotePaperLikeCount(votePaperId);
    }

    public boolean isVotePaperLikePresent(Long votePaperId, User user) {
        return loadLikePort.loadVotePaperLike(votePaperId, user.getUsername()).isPresent();
    }

}
