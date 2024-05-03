package com.tune_fun.v1.vote.application.port.input.usecase;

import org.springframework.security.core.userdetails.User;

@FunctionalInterface
public interface DeleteVotePaperUseCase {
    void delete(final Long votePaperId, final User user);
}
