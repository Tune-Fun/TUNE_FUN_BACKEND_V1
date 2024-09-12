package com.tune_fun.v1.vote.application.port.input.usecase;

import com.tune_fun.v1.vote.domain.value.ScrollableVotePaper;
import org.springframework.data.domain.Window;

import java.time.LocalDateTime;

public interface ScrollVotePaperUseCase {

    Window<ScrollableVotePaper> scrollVotePaper(Integer lastId, String sortType, String nickname);

    Window<ScrollableVotePaper> scrollUserLikedVotePaper(String username, Long lastId, LocalDateTime lastTime, Integer count);

    Window<ScrollableVotePaper> scrollUserVotedVotePaper(String username, Long lastId, LocalDateTime lastTime, Integer count);
}
