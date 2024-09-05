package com.tune_fun.v1.account.application.port.input.usecase;

import com.tune_fun.v1.interaction.domain.ArtistInfo;
import com.tune_fun.v1.interaction.domain.ScrollableArtist;

@FunctionalInterface
public interface FindArtistUseCase {

    ArtistInfo findArtist(final Long artistId);
}
