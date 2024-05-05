package com.tune_fun.v1.vote.domain.behavior;

import com.tune_fun.v1.vote.domain.value.VotePaperOption;

import java.time.LocalDateTime;

public record SaveVotePaper(
        String title,
        String content,
        String author,
        VotePaperOption option,
        LocalDateTime voteStartAt,
        LocalDateTime voteEndAt
) {
}
