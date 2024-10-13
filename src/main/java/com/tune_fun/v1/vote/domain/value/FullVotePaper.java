package com.tune_fun.v1.vote.domain.value;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tune_fun.v1.common.response.BasePayload;

import java.time.LocalDateTime;
import java.util.Set;

public record FullVotePaper(
        Long id,
        String uuid,
        String author,
        String authorUsername,
        String profileImageUrl,
        String title,
        String content,
        Long totalVoteCount,
        Long totalLikeCount,
        VotePaperOption option,
        String videoUrl,

        Boolean isVoted,

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime voteStartAt,

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime voteEndAt,

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime deliveryAt,

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime createdAt,

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime updatedAt,

        Set<RegisteredVoteChoice> choices
) implements BasePayload {

}
