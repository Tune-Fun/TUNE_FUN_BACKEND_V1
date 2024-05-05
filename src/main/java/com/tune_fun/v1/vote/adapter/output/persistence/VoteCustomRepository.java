package com.tune_fun.v1.vote.adapter.output.persistence;

import com.tune_fun.v1.vote.domain.value.RegisteredVote;

import java.util.List;
import java.util.Optional;

public interface VoteCustomRepository {

    List<Long> findVoterIdsByVotePaperUuid(final String uuid);

    Optional<RegisteredVote> findByVoterUsernameAndVotePaperId(final String voter, final Long votePaperId);

}
