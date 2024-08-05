package com.tune_fun.v1.account.application.port.input.usecase;

import com.tune_fun.v1.interaction.domain.ScrollableArtist;
import org.springframework.data.domain.Window;

@FunctionalInterface
public interface ScrollArtistUseCase {

    Window<ScrollableArtist> scrollArtist(final Integer lastId, final String nickname);

}
