package com.tune_fun.v1.vote.domain.value;

public record RegisteredVote(
        Long id,
        String uuid,
        String username,
        Long votePaperId,
        String music,
        String artistName
) {
}
