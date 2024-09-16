package com.tune_fun.v1.account.adapter.input.rest;

import com.tune_fun.v1.account.application.port.input.usecase.FindArtistUseCase;
import com.tune_fun.v1.account.application.port.input.usecase.ScrollArtistUseCase;
import com.tune_fun.v1.common.config.Uris;
import com.tune_fun.v1.common.response.MessageCode;
import com.tune_fun.v1.common.response.Response;
import com.tune_fun.v1.common.response.ResponseMapper;
import com.tune_fun.v1.common.stereotype.WebAdapter;
import com.tune_fun.v1.interaction.domain.ArtistInfo;
import com.tune_fun.v1.interaction.domain.ScrollableArtist;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Window;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@WebAdapter
@RequiredArgsConstructor
public class ArtistController {

    private final ResponseMapper responseMapper;

    private final ScrollArtistUseCase scrollArtistUseCase;
    private final FindArtistUseCase findArtistUseCase;

    @GetMapping(value = Uris.ARTIST_ROOT)
    public ResponseEntity<Response<ScrollArtistResponse>> scrollArtist(
            @RequestParam(name = "last_id", required = false) Long lastId,
            @RequestParam(name = "nickname", required = false) String nickname) {
        Slice<ScrollableArtist> homeSearchResult = scrollArtistUseCase.scrollArtist(lastId, nickname);
        return responseMapper.ok(MessageCode.SUCCESS, new ScrollArtistResponse(homeSearchResult));
    }

    @GetMapping(value = Uris.ARTIST_ROOT + "/{artistId}")
    public ResponseEntity<Response<ArtistInfo>> findArtist(@PathVariable Long artistId){
        return responseMapper.ok(MessageCode.SUCCESS, findArtistUseCase.findArtist(artistId));
    }


}
