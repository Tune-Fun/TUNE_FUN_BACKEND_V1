package com.tune_fun.v1.vote.application.service;

import com.tune_fun.v1.common.stereotype.UseCase;
import com.tune_fun.v1.vote.application.port.input.usecase.ScrollVotePaperUseCase;
import com.tune_fun.v1.vote.application.port.output.LoadVotePaperPort;
import com.tune_fun.v1.vote.domain.value.ScrollableVotePaper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Window;
import org.springframework.stereotype.Service;

@Service
@UseCase
@RequiredArgsConstructor
public class ScrollVotePaperService implements ScrollVotePaperUseCase {

    private final LoadVotePaperPort loadVotePaperPort;

    @Override
    public Window<ScrollableVotePaper> scrollVotePaper(final Integer lastId, final String sortType, final String nickname) {
        return loadVotePaperPort.scrollVotePaper(lastId, sortType, nickname);
    }
}
