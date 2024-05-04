package com.tune_fun.v1.vote.application.port.output;

import com.tune_fun.v1.vote.domain.value.RegisteredVoteChoice;

import java.util.List;
import java.util.Optional;

public interface LoadVoteChoicePort {
    List<RegisteredVoteChoice> loadRegisteredVoteChoice(final Long VotePaperId);

    Optional<RegisteredVoteChoice> loadVoteChoiceByUsername(final Long votePaperId, final String username);
}
