package com.tune_fun.v1.vote.application.port.output;

import com.tune_fun.v1.vote.domain.value.RegisteredVote;

import java.util.List;
import java.util.Optional;

public interface LoadVotePort {
    List<Long> loadVoterIdsByVotePaperUuid(final String uuid);

    Optional<RegisteredVote> loadVoteByVoterAndVotePaperId(final String voter, final Long votePaperId);
}
