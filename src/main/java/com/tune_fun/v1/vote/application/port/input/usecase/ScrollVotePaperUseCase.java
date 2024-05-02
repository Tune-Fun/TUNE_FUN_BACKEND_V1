package com.tune_fun.v1.vote.application.port.input.usecase;

import com.tune_fun.v1.vote.domain.value.ScrollableVotePaper;
import org.springframework.data.domain.Window;

@FunctionalInterface
public interface ScrollVotePaperUseCase {

    Window<ScrollableVotePaper> scrollVotePaper(Integer lastIdx, String sortType);

}
