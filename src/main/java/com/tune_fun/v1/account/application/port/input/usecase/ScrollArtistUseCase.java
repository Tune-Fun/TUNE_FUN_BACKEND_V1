package com.tune_fun.v1.account.application.port.input.usecase;

import com.tune_fun.v1.interaction.domain.ScrollableArtist;
import org.springframework.data.domain.Slice;

@FunctionalInterface
public interface ScrollArtistUseCase {

    Slice<ScrollableArtist> scrollArtist(final Long lastId, final String nickname);

}
