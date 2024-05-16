package com.tune_fun.v1.interaction.application.service;

import com.tune_fun.v1.common.hexagon.UseCase;
import com.tune_fun.v1.interaction.application.port.input.usecase.UnlikeVotePaperUseCase;
import com.tune_fun.v1.interaction.application.port.output.DeleteLikePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@UseCase
@RequiredArgsConstructor
public class UnlikeVotePaperService implements UnlikeVotePaperUseCase {

    private final DeleteLikePort deleteLikePort;

    @Transactional
    @Override
    public void unlikeVotePaper(final Long likeId) {
        deleteLikePort.deleteVotePaperLike(likeId);
    }


}
