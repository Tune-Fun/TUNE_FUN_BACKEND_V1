package com.tune_fun.v1.vote.adapter.output.persistence;

import java.util.List;
import java.util.Optional;

public interface VoteCustomRepository {

    List<Long> findVoterIdsByVotePaperUuid(final String uuid);

    Optional<VoteJpaEntity> findByVoterUsernameAndVotePaperId(final String voter, final Long votePaperId);

}
