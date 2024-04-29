package com.tune_fun.v1.vote.domain.behavior;

import java.util.Set;

public record SaveVoteChoice(
        String music,
        String offerArtistName,
        Set<String> offerGenres,
        Integer offerDurationMs,
        String offerReleaseDate
) {

}
