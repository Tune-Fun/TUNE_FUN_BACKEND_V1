package com.tune_fun.v1.interaction.application.port.input.usecase;

import com.tune_fun.v1.interaction.domain.VotePaperSearchResult;

@FunctionalInterface
public interface VotePaperSearchUseCase {

    VotePaperSearchResult searchVotePaper(final Integer lastId);

}
