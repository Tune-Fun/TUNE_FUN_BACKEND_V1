package com.tune_fun.v1.vote.domain.value;

import java.time.LocalDateTime;

public record RegisteredVotePaper(
        Long id,
        String uuid,
        Long authorId,
        String author,
        String authorUsername,
        String profileImageUrl,
        String title,
        String content,
        VotePaperOption option,
        String videoUrl,
        LocalDateTime voteStartAt,
        LocalDateTime voteEndAt,
        LocalDateTime deliveryAt,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public boolean isAuthor(final String username) {
        return authorUsername.equals(username);
    }

    public boolean isValidDeliveryAt(final LocalDateTime now, final LocalDateTime deliveryAt) {
        return deliveryAt.isAfter(now) && deliveryAt.isAfter(voteEndAt);
    }

}
