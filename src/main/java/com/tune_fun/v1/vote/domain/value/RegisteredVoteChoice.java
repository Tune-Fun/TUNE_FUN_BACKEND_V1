package com.tune_fun.v1.vote.domain.value;

import com.tune_fun.v1.common.response.BasePayload;

public record RegisteredVoteChoice(
        Long id,
        Long votePaperId,
        String offerId,
        String music,
        String artistName
) implements BasePayload {
}
