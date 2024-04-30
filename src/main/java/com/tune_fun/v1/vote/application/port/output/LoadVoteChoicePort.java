package com.tune_fun.v1.vote.application.port.output;

import com.tune_fun.v1.vote.domain.value.RegisteredVoteChoice;

import java.util.List;

public interface LoadVoteChoicePort {
    List<RegisteredVoteChoice> loadRegisteredVoteChoice(final Long VotePaperId);
}
