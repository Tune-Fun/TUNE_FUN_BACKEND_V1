package com.tune_fun.v1.vote.adapter.input.rest;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.tune_fun.v1.common.response.BasePayload;
import com.tune_fun.v1.vote.domain.value.ScrollableVotePaper;
import org.springframework.data.domain.Window;

public record ScrollVotePaperResponse(@JsonUnwrapped Window<ScrollableVotePaper> votePapers) implements BasePayload {
}
