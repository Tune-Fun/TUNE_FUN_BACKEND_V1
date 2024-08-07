package com.tune_fun.v1.interaction.domain;

import com.tune_fun.v1.common.response.BasePayload;

import java.util.List;

public record HomeSearchResult(
        List<ScrollableArtist> users,
        List<VotePaperSearchResult> votePapers
) implements BasePayload {
}
