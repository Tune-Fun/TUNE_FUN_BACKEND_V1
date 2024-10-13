package com.tune_fun.v1.vote.application.port.output;

public interface SaveVotePaperStatisticsPort {

    void initializeStatistics(final Long votePaperId);

    void updateLikeCount(final Long votePaperId, final Long likeCount);
    void updateVoteCount(final Long votePaperId, final Long voteCount);

}
