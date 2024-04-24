package com.tune_fun.v1.vote.domain;

import java.time.LocalDateTime;

public record RegisteredVotePaper(
        String id,
        String uuid,
        String title,
        String content,
        String option,
        LocalDateTime voteStartAt,
        LocalDateTime voteEndAt,
        LocalDateTime deliveryAt,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
