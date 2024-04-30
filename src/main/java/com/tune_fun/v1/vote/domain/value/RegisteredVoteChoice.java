package com.tune_fun.v1.vote.domain.value;

import java.util.Set;

public record RegisteredVoteChoice(
        Long id,
        Long votePaperId,
        String music,
        String artistName,
        Set<String> genres,
        String releaseDate,
        Integer durationMs
) {
}
