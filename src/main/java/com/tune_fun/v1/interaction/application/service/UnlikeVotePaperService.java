package com.tune_fun.v1.interaction.application.service;

import com.tune_fun.v1.common.hexagon.UseCase;
import com.tune_fun.v1.interaction.application.port.input.usecase.UnlikeVotePaperUseCase;
import com.tune_fun.v1.interaction.application.port.output.DeleteLikePort;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

@Service
@UseCase
@RequiredArgsConstructor
public class UnlikeVotePaperService implements UnlikeVotePaperUseCase {

    private final DeleteLikePort deleteLikePort;


    @Override
    public void unlikeVotePaper(Long votePaperId, User user) {

    }


}
