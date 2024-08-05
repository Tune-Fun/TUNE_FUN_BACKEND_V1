package com.tune_fun.v1.account.application.port.input.usecase;

import com.tune_fun.v1.interaction.domain.ArtistSearchResult;

@FunctionalInterface
public interface ArtistSearchUseCase {

    ArtistSearchResult searchArtist(final Integer lastId);

}
