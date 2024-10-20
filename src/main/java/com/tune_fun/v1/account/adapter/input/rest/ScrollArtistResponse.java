package com.tune_fun.v1.account.adapter.input.rest;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.tune_fun.v1.common.response.BasePayload;
import com.tune_fun.v1.interaction.domain.ScrollableArtist;
import org.springframework.data.domain.Slice;

public record ScrollArtistResponse(@JsonUnwrapped Slice<ScrollableArtist> artists) implements BasePayload {
}
