package com.tune_fun.v1.interaction.application.service;

import com.tune_fun.v1.common.hexagon.UseCase;
import com.tune_fun.v1.interaction.application.port.input.usecase.LikeVotePaperUseCase;
import com.tune_fun.v1.interaction.application.port.output.SaveLikePort;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

@Service
@UseCase
@RequiredArgsConstructor
public class LikeVotePaperService implements LikeVotePaperUseCase {

    private final SaveLikePort saveLikePort;


    @Override
    public void likeVotePaper(Long votePaperId, User user) {

    }

}
