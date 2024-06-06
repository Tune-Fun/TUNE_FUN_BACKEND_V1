package com.tune_fun.v1.interaction.application.service;

import com.tune_fun.v1.common.stereotype.UseCase;
import com.tune_fun.v1.interaction.application.port.input.usecase.UnlikeVotePaperUseCase;
import com.tune_fun.v1.interaction.application.port.output.DeleteLikePort;
import com.tune_fun.v1.interaction.application.port.output.SaveVotePaperLikeCountPort;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@UseCase
@RequiredArgsConstructor
public class UnlikeVotePaperService implements UnlikeVotePaperUseCase {

    private final DeleteLikePort deleteLikePort;
    private final SaveVotePaperLikeCountPort saveVotePaperLikeCountPort;

    @Transactional
    @Override
    public void unlikeVotePaper(final Long votePaperId, final User user) {
        deleteLikePort.deleteVotePaperLike(votePaperId, user.getUsername());
        saveVotePaperLikeCountPort.decrementVotePaperLikeCount(votePaperId);
    }


}
