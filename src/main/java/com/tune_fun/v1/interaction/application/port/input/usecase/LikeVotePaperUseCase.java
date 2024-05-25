package com.tune_fun.v1.interaction.application.port.input.usecase;

import org.springframework.security.core.userdetails.User;

@FunctionalInterface
public interface LikeVotePaperUseCase {

    void likeVotePaper(final Long votePaperId, final User user);

}
