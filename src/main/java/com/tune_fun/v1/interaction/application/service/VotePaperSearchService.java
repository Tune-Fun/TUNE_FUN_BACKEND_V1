package com.tune_fun.v1.interaction.application.service;

import com.tune_fun.v1.common.stereotype.UseCase;
import com.tune_fun.v1.interaction.application.port.input.usecase.VotePaperSearchUseCase;
import com.tune_fun.v1.interaction.domain.VotePaperSearchResult;
import com.tune_fun.v1.vote.application.port.output.LoadVotePaperPort;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class VotePaperSearchService implements VotePaperSearchUseCase {

    private final LoadVotePaperPort loadVotePaperPort;

    @Override
    public VotePaperSearchResult searchVotePaper(Integer lastId) {
        return null;
    }

}
