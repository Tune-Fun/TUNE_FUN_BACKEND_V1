package com.tune_fun.v1.vote.domain.value;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record ScrollableVotePaper(
        Long id,
        String uuid,
        String title,
        String authorUsername,
        String authorProfileImageUrl,
        String authorNickname,
        Long remainDays,
        Long totalVoteCount,
        Long totalLikeCount,
        Boolean isVoted
) {
}
