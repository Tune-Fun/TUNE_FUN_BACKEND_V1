package com.tune_fun.v1.interaction.adapter.input.rest;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.tune_fun.v1.common.response.BasePayload;
import com.tune_fun.v1.interaction.domain.ScrollableFollowInfo;
import org.springframework.data.domain.Window;

public record ScrollableFollowInfoResponse(
        @JsonUnwrapped Window<ScrollableFollowInfo> followInfos) implements BasePayload {
}
