package com.tune_fun.v1.interaction.domain;

import com.tune_fun.v1.common.response.BasePayload;

public record VotePaperSearchResult(
        Long id,
        String uuid,
        String title,
        String authorUsername,
        String authorNickname,
        String authorProfileImageUrl,
        Long remainDays,
        Long totalVoteCount,
        Long totalLikeCount
) implements BasePayload {
}
