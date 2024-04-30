package com.tune_fun.v1.vote.application.port.input.usecase;

import jakarta.validation.constraints.NotNull;
import org.springframework.security.core.userdetails.User;

@FunctionalInterface
public interface RegisterVoteUseCase {

    void register(@NotNull Long votePaperId, final @NotNull Long voteChoiceId, User user);

}
