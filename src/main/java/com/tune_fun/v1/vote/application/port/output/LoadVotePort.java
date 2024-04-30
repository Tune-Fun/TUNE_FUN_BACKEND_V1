package com.tune_fun.v1.vote.application.port.output;

import java.util.List;

public interface LoadVotePort {
    List<Long> loadVoterIdsByVotePaperUuid(final String uuid);
}
