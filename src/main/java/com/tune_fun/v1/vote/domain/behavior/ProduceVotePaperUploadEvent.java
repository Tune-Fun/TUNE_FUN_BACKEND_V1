package com.tune_fun.v1.vote.domain.behavior;

import java.time.LocalDateTime;

public record ProduceVotePaperUploadEvent(
        String id,
        String title,
        String content,
        LocalDateTime voteStartAt,
        LocalDateTime voteEndAt
) {
}
