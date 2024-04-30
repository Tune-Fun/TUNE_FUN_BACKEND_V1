package com.tune_fun.v1.vote.adapter.output.persistence;

import java.util.List;

public interface VoteCustomRepository {

    List<Long> findVoterIdsByVotePaperUuid(final String uuid);

}
