package com.tune_fun.v1.interaction.application.service;

import com.tune_fun.v1.account.application.port.output.LoadAccountPort;
import com.tune_fun.v1.common.stereotype.UseCase;
import com.tune_fun.v1.interaction.application.port.input.usecase.ArtistSearchUseCase;
import com.tune_fun.v1.interaction.domain.ArtistSearchResult;
import com.tune_fun.v1.interaction.domain.HomeSearchResult;
import com.tune_fun.v1.vote.application.port.output.LoadVotePaperPort;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@RequiredArgsConstructor
public class ArtistSearchService implements ArtistSearchUseCase {

    private final LoadAccountPort loadAccountPort;

    @Transactional(readOnly = true)
    @Override
    public ArtistSearchResult searchArtist(Integer lastId) {
        return null;
    }

}
