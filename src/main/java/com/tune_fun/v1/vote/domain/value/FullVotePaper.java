package com.tune_fun.v1.vote.domain.value;

import java.time.LocalDateTime;
import java.util.Set;

public record FullVotePaper(
        Long id,
        String uuid,
        String authorUsername,
        String authorNickname,
        String title,
        String content,
        VotePaperOption option,
        String videoUrl,
        LocalDateTime voteStartAt,
        LocalDateTime voteEndAt,
        LocalDateTime deliveryAt,
        Long remainDays,
        String totalVoteCount,
        String totalLikeCount,
        Set<Choice> choices
) {
}
