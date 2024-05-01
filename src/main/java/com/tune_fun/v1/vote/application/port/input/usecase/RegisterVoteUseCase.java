package com.tune_fun.v1.vote.application.port.input.usecase;

import org.springframework.security.core.userdetails.User;

@FunctionalInterface
public interface RegisterVoteUseCase {

    void register(Long votePaperId, final Long voteChoiceId, User user);

}
