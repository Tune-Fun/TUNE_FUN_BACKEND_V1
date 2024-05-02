package com.tune_fun.v1.vote.adapter.input.rest;

import com.tune_fun.v1.common.response.BasePayload;
import com.tune_fun.v1.vote.domain.value.ScrollableVotePaper;
import org.springframework.data.domain.Window;

public record ScrollVotePaperResponse(Window<ScrollableVotePaper> votePapers) implements BasePayload {
}
