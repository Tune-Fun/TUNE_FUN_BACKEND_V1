package com.tune_fun.v1.vote.adapter.output.persistence;

import java.util.Map;
import java.util.Set;

public interface VotePaperStatisticsCustomRepository {
    void updateLikeCount(final Long votePaperId, final Long likeCount);

    Map<Long, Long> findLikeCountMap(final Set<Long> votePaperIds);
}
