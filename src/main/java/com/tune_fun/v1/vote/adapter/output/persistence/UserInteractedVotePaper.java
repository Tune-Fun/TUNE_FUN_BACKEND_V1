package com.tune_fun.v1.vote.adapter.output.persistence;

import com.tune_fun.v1.vote.domain.value.VotePaperOption;

import java.time.LocalDateTime;

public record UserInteractedVotePaper(
        Long id,
        String uuid,
        String title,
        String content,
        String pageLink,
        String authorUsername,
        String authorNickname,
        VotePaperOption option,
        LocalDateTime voteStartAt,
        LocalDateTime voteEndAt,
        LocalDateTime deliveryAt,
        boolean enabled,
        String videoUrl,
        LocalDateTime interactedAt
) {
}
