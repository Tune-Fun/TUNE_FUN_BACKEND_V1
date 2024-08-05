package com.tune_fun.v1.account.adapter.input.rest;

import com.tune_fun.v1.account.application.port.input.usecase.ScrollArtistUseCase;
import com.tune_fun.v1.common.config.Uris;
import com.tune_fun.v1.common.response.MessageCode;
import com.tune_fun.v1.common.response.Response;
import com.tune_fun.v1.common.response.ResponseMapper;
import com.tune_fun.v1.common.stereotype.WebAdapter;
import com.tune_fun.v1.interaction.domain.ScrollableArtist;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Window;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@WebAdapter
@RequiredArgsConstructor
public class ArtistController {

    private final ResponseMapper responseMapper;

    private final ScrollArtistUseCase scrollArtistUseCase;

    @GetMapping(value = Uris.ARTIST_ROOT)
    public ResponseEntity<Response<ScrollArtistResponse>> fetchArtists(
            @RequestParam(name = "last_id", required = false) Integer lastId,
            @RequestParam(name = "nickname", required = false) String nickname) {
        Window<ScrollableArtist> homeSearchResult = scrollArtistUseCase.scrollArtist(lastId, nickname);
        return responseMapper.ok(MessageCode.SUCCESS, new ScrollArtistResponse(homeSearchResult));
    }

}
