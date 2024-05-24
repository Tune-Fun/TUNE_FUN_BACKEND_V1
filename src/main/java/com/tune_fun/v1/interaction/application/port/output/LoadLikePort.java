package com.tune_fun.v1.interaction.application.port.output;

import com.tune_fun.v1.vote.domain.value.RegisteredVotePaperLike;

import java.util.Optional;

public interface LoadLikePort {
    Optional<RegisteredVotePaperLike> loadVotePaperLike(final Long votePaperId, final String username);
}
