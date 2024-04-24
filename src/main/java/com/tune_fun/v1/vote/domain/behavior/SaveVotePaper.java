package com.tune_fun.v1.vote.domain.behavior;

import java.time.LocalDateTime;

public record SaveVotePaper(
        String title,
        String content,
        String author,
        String option,
        LocalDateTime voteStartAt,
        LocalDateTime voteEndAt
) {
}
