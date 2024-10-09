package com.tune_fun.v1.vote.domain.value;

public record VotePaperStatistics(
        Long votePaperId,
        Long voteCount,
        Long likeCount
) {
}
