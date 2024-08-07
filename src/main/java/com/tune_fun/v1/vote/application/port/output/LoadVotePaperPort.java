package com.tune_fun.v1.vote.application.port.output;

import com.tune_fun.v1.vote.domain.value.RegisteredVotePaper;
import com.tune_fun.v1.vote.domain.value.ScrollableVotePaper;
import org.springframework.data.domain.Window;

import java.util.Optional;

public interface LoadVotePaperPort {

    Window<ScrollableVotePaper> scrollVotePaper(final Integer lastId, final String sortType, final String nickname);

    Optional<RegisteredVotePaper> loadRegisteredVotePaper(final String username);

    Optional<RegisteredVotePaper> loadRegisteredVotePaper(final Long votePaperId);

}
